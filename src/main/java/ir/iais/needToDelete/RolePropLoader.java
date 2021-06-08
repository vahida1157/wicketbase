package ir.iais.needToDelete;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.*;

/**
 * @author vahid
 * create on 6/6/2021
 */

public class RolePropLoader {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(RolePropLoader.class);

    private Map<String, String> roles;
    private Map<String, Set<String>> rolesStructuredMap;
    private Boolean SmsVerify;
    private Boolean filterUserRole;

    private RolePropLoader() {
        try {
            PropertiesConfiguration rolesConfiguration = new PropertiesConfiguration("roles.properties");
            roles = new HashMap<>();
            Iterator<String> keys = rolesConfiguration.getKeys();
            for (; keys.hasNext(); ) {
                String next = keys.next();
                roles.put(next, rolesConfiguration.getString(next));
            }
            roles = Collections.unmodifiableMap(roles);
        } catch (ConfigurationException ex) {
            LOGGER.fatal(ex);
        }
        /*
         * set roles structure , if the file doesn't exist add all roles to
         * each role
         */
        boolean fileExisted = true;
        PropertiesConfiguration rolesStructureMapConfiguration = null;
        try {
            rolesStructureMapConfiguration = new PropertiesConfiguration("rolesStructuredMap.properties");
        } catch (ConfigurationException ex) {
            fileExisted = false;
            LOGGER.fatal(ex);
        }
        rolesStructuredMap = new HashMap<>();
        if (fileExisted) {
            Iterator<String> stateStructureMapIter = rolesStructureMapConfiguration.getKeys();
            while (stateStructureMapIter.hasNext()) {
                String key = stateStructureMapIter.next();
                final String[] availState = rolesStructureMapConfiguration.getStringArray(key);
                ArrayList<String> strings = new ArrayList<>(Arrays.asList(availState));
                rolesStructuredMap.put(key, new HashSet<>(strings));
            }
        } else {
            if (roles != null) {
                for (String role : roles.keySet()) {
                    rolesStructuredMap.put(role, roles.keySet());
                }
            }
        }
        rolesStructuredMap = Collections.unmodifiableMap(rolesStructuredMap);
        /*
         *
         */
        LOGGER.info("Start checking SMS verify");
        SmsVerify = false;
        try {
            PropertiesConfiguration primeConfiguration = new PropertiesConfiguration("PrimeConfig.properties");
            Iterator<String> keys = primeConfiguration.getKeys("SmsVerify");
            filterUserRole = primeConfiguration.getBoolean("filterUserRole", false);
            for (; keys.hasNext(); ) {
                String next = keys.next();
                if ("SmsVerify".equals(next)) {
                    String string = primeConfiguration.getString(next);
                    SmsVerify = Boolean.valueOf(string);
                }
            }
            LOGGER.info("set SMS Verify to :: " + SmsVerify);
        } catch (ConfigurationException ex) {
            LOGGER.fatal(ex);
            SmsVerify = false;
        }

    }

    public static RolePropLoader getInstance() {
        return RolePropLoaderHolder.INSTANCE;
    }

    private static class RolePropLoaderHolder {

        private static final RolePropLoader INSTANCE = new RolePropLoader();
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    public Boolean getSmsVerify() {
        return SmsVerify;
    }

    public Map<String, Set<String>> getRolesStructureMap() {
        return rolesStructuredMap;
    }

    public Boolean getFilterUserRole() {
        return filterUserRole;
    }

    public void setFilterUserRole(Boolean filterUserRole) {
        this.filterUserRole = filterUserRole;
    }


}
