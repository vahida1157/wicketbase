/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.iais.authentication.sso;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;


import javax.crypto.SecretKey;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * @author Zahra
 */
public class SSOServices {

    private static SSOServices instance;

    private SSOServices() {
    }

    public static SSOServices getInstance() {
        if (instance == null) {
            instance = new SSOServices();
        }
        return instance;
    }

    private WebTarget getTarget(String url) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.property(ClientProperties.CONNECT_TIMEOUT, 3000);
        clientConfig.property(ClientProperties.READ_TIMEOUT, 20000);
        Client client = ClientBuilder.newClient(clientConfig);
        return client.target(url);
    }

    public void renewAccess(String subject) {
        String url = SSOProperties.getInstance().getApiRest() + "oauth/renewaccess/";
        SecretKey signKey = Keys.hmacShaKeyFor((SSOProperties.getInstance().getKey()).getBytes());
        String jwtToken = Jwts.builder().setSubject(subject)
                .signWith(signKey).setIssuer(SSOProperties.getInstance().getAcronym()).setAudience("SSO").setIssuedAt(new Date())
                .compact();
        MultivaluedMap<String, String> data = new MultivaluedHashMap<>();
        data.add("acronym", SSOProperties.getInstance().getAcronym());
        data.add("token", jwtToken);
        try {
            WebTarget target = getTarget(url);
            Response response = target.request().accept(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(Entity.form(data));

            String result = response.readEntity(String.class);
            int status = response.getStatus();
            Logger.getLogger(this.getClass()).debug("status: " + status + " url: " + url + " subject: " + subject + " result: " + result);
            if (status == 200) {
                SSOResult sSOResult = new Gson().fromJson(result, SSOResult.class);
                Logger.getLogger(this.getClass()).debug(subject + ": " + sSOResult.getStatus() + " renewAccess sso subject :)");
            } else {
                Logger.getLogger(this.getClass()).error(response.getStatus() + result);
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass()).error("subject: " + subject + " error in renewAccess sso subject:|", ex);
        }
    }
}
