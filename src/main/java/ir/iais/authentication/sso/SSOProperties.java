/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.iais.authentication.sso;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author Zahra
 */
public class SSOProperties {

    private Properties file;
    static private SSOProperties instance;
    private Integer status;
    private Date startDateOfMigrateToSSO;
    private Integer timeToLiveOfUserInOldPatternOfLogin;
    private String acronym;
    private String callbackUrl;
    private List<String> neededScopes;

    private SSOProperties() {
        try {
            file = new Properties();
            file.load(SSOProperties.class.getResourceAsStream("/ssoconfig.properties"));
        } catch (Exception ex) {
            Logger.getLogger(SSOProperties.class).error("cannot load SSOConfig properties", ex);
        }
    }

    public static SSOProperties getInstance() {
        if (instance == null) {
            instance = new SSOProperties();
        }
        return instance;
    }

    public String getKey() {
        return file.getProperty("AESKEY");
    }

    public String getApiRest() {
        return file.getProperty("apiRest");
    }

    public String getSsoURL() {
        return file.getProperty("ssoURL");
    }

    /**
     * @return status
     * @ 0 ==> not affect of sso in login page
     * @ 1 ==> sso is base of login
     * @ 2 ==> user see a link in login page for a login with sso
     * @ 3 ==> user see a link in login page for a login with sso and users should
     * migrate to sso login pattern after a time base of two function
     * getStartDateOfMigrateToSSO() and
     * getTimeToLiveOfUserInOldPatternOfLogin()
     */
    public Integer getStatus() {
        if (status == null) {
            status = Integer.parseInt(file.getProperty("status", "0"));
        }
        return status;
    }

    /**
     * @return startDateOfMigrateToSSO
     * @ start of date to force user migrate to sso login pattern base of
     * timeToLiveOfUserInOldPatternOfLogin
     */
    public Date getStartDateOfMigrateToSSO() {
        if (startDateOfMigrateToSSO == null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                startDateOfMigrateToSSO = simpleDateFormat.parse(file.getProperty("startDateOfMigrateToSSO", simpleDateFormat.format(new Date())));
            } catch (ParseException ex) {
                startDateOfMigrateToSSO = new Date();
                Logger.getLogger(this.getClass()).error(ex + ". cannot read startDateOfMigrateToSSO. continue with now time", ex);
            }
        }
        return startDateOfMigrateToSSO;
    }

    /**
     * @return timeToLiveOfUserInOldPatternOfLogin
     * @ user force migrate base of this parameter slowly. if user that her/his
     * national code mod to this parameter in addition of
     * startDateOfMigrateToSSO is less now time should login with sso
     * pattern.
     */
    public Integer getTimeToLiveOfUserInOldPatternOfLogin() {
        if (timeToLiveOfUserInOldPatternOfLogin == null) {
            timeToLiveOfUserInOldPatternOfLogin = Integer.parseInt(file.getProperty("timeToLiveOfUserInOldPatternOfLogin", "30"));
        }
        return timeToLiveOfUserInOldPatternOfLogin;
    }

    /**
     * @return acronym
     * @ client name that accept by sso
     */
    public String getAcronym() {
        if (acronym == null) {
            acronym = file.getProperty("acronym");
        }
        return acronym;
    }

    /**
     * @return callbackUrl
     * @ sso redirect user to this URL
     */
    public String getCallbackUrl() {
        if (callbackUrl == null) {
            callbackUrl = file.getProperty("callbackUrl");
        }
        return callbackUrl;
    }

    /**
     * @return neededFields as list
     * @ fields needed by system should be divided by dash is sso properties
     * file
     */
    public List<String> getNeededScopes() {
        if (neededScopes == null) {
            neededScopes = Arrays.asList(file.getProperty("neededFields", "FIRSTNAME-LASTNAME-NATCODE-FATHERNAME-BIRTHDATE-CELLPHONE").split("-"));
        }
        return neededScopes;
    }
}
