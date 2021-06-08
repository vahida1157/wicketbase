/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.iais.authentication.sso;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ir.iais.auditing.AuditLog;
import ir.iais.auditing.AuditPriority;
import ir.iais.domain.SSOToken;
import ir.iais.domain.UserClass;
import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.json.JsonObject;
import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Karam
 */
public class SSOLogin extends WebPage {

    public SSOLogin(PageParameters param) {
        String token = param.get("userdata").toOptionalString();
        Logger.getLogger(this.getClass()).debug("param:" + new Gson().toJson(param));
        Logger.getLogger(this.getClass()).debug("userdata:" + token);
        String keyString = SSOProperties.getInstance().getKey();
        Key signKey = Keys.hmacShaKeyFor(keyString.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
        Logger.getLogger(this.getClass()).debug("claims:" + claims.values());
        String key = SSOProperties.getInstance().getKey();
        String decryptedData = SSOAesCrypt.decrypt(key, claims.get("encryptedData", String.class));
        Logger.getLogger(this.getClass()).debug("decryptedData:" + decryptedData);
        JsonObject resultJsonObject = new Gson().fromJson(decryptedData, JsonObject.class);
        Logger.getLogger(this.getClass()).debug("resultJsonObject:" + new Gson().toJson(resultJsonObject));
        String accessToken = resultJsonObject.get("access_token") == null ? null : resultJsonObject.get("access_token").toString();
        String refreshToken = resultJsonObject.get("refresh_token") == null ? null : resultJsonObject.get("refresh_token").toString();
        String idToken = resultJsonObject.get("id_token") == null ? null : resultJsonObject.get("id_token").toString();
        processOnSSOTokens(accessToken, refreshToken, idToken, claims.getSubject());
    }

    protected void processOnSSOTokens(String accessToken, String refreshToken, String idToken, String subject) {

        try {
            UserClass user = UserClass.loadBySSOSubject(subject);
            if (idToken != null) {
                String body = new String(Base64.getDecoder().decode(idToken.split("\\.")[1].replace('-', '+').replace('_', '/')));
                JsonObject resultJsonObject = new Gson().fromJson(body, JsonObject.class);
                String nationalId = resultJsonObject.get("natcode").toString();
                String firstName = resultJsonObject.get("firstname").toString();
                String lastName = resultJsonObject.get("lastname").toString();
                String cellphone = resultJsonObject.get("cellphone").toString();
                if (user == null) {
                    user = UserClass.loadByUsernameAndCodeMelli(nationalId, nationalId);
                    if (user == null) {
                        user = createNewUserWithUserDefaultRole(nationalId, cellphone, firstName, lastName, accessToken, refreshToken, subject);
                    } else {
                        UserClass.updateOldUser(user, cellphone, firstName, lastName, accessToken, refreshToken, subject);
                    }
                } else {
                    UserClass.updateOldUser(user, cellphone, firstName, lastName, accessToken, refreshToken, subject);
                }
            } else {
                if (user == null) {
                    SSOServices.getInstance().renewAccess(subject);
                    String ssoLoginLink = SSOUtils.getInstance().getSSOLoginLink();
                    setResponsePage(new RedirectPage(ssoLoginLink));
                }
            }
            if (user != null && user.getUsername() != null && !user.getUsername().equals("")) {
                boolean authResult = AuthenticatedWebSession.get().signIn(user.getUsername(), user.getPassword());
                if (authResult) {
                    AuditLog.LOG("User " + user.getMeliCode() + " signed in", AuditPriority.MEDIUM, -1);
                    try {
                        continueToOriginalDestination();
                    } catch (Exception ex) {
                        Logger.getLogger(this.getClass()).error(ex, ex);
                    }
                    setResponsePage(getApplication().getHomePage());
                } else {
                    setResponsePage(LoginRedirectSSOPage.class);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass()).error(ex, ex);
            setResponsePage(LoginRedirectSSOPage.class);
        }
    }

    private UserClass createNewUserWithUserDefaultRole(String nationalId, String cellphone, String firstName, String lastName, String accessToken, String refreshToken, String subject) {
        UserClass userClass = new UserClass(nationalId, "", cellphone, "USER", nationalId, firstName + " " + lastName, new HashSet<>(), new HashSet<>(Collections.singletonList("USER")));
        userClass.setsSOSubject(subject);
        userClass.setAccessToken(new SSOToken(accessToken));
        userClass.setRefreshToken(new SSOToken(refreshToken));
        userClass.setSsoUpdateTime(System.currentTimeMillis());
        userClass.save();
        return userClass;
    }
}
