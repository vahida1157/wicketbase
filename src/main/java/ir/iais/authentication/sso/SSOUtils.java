/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.iais.authentication.sso;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.List;

/**
 * @author karam
 */
public class SSOUtils {

    private static SSOUtils instance;

    private SSOUtils() {
    }

    public static SSOUtils getInstance() {
        if (instance == null) {
            instance = new SSOUtils();
        }
        return instance;
    }

    public String getSSOLoginLink() {
        List<String> neededFields = SSOProperties.getInstance().getNeededScopes();
        HashMap<String, Object> payloadHashMap = new HashMap<>();
        payloadHashMap.put("scopes", neededFields);
        payloadHashMap.put("callbackUrl", SSOProperties.getInstance().getCallbackUrl());
        SecretKey signKey = Keys.hmacShaKeyFor((SSOProperties.getInstance().getKey()).getBytes());
        String jwtToken = Jwts.builder().addClaims(payloadHashMap)
                .signWith(signKey).setIssuer(SSOProperties.getInstance().getAcronym()).setAudience("SSO")
                .compact();
        return SSOProperties.getInstance().getSsoURL() + "?token=" + jwtToken;
    }
}
