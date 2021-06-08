package ir.iais.authentication;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author vahid
 * create on 6/6/2021
 */
public class ActiveUsers {

    private static final ConcurrentMap<String, BasicAuthenticationSession> ALL_USERS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, String> SESSION_IDS = new ConcurrentHashMap<>();

    public static void add(String username, BasicAuthenticationSession bas) {
        ALL_USERS.put(username, bas);
        SESSION_IDS.put(bas.getSessionId(), username);
    }

    public static BasicAuthenticationSession remove(String username) {
        BasicAuthenticationSession bas = ALL_USERS.remove(username);
        try {
            if (bas != null) {
                SESSION_IDS.remove(bas.getSessionId());
            }
        } catch (Exception e) {
        }
        return bas;
    }

    public static BasicAuthenticationSession get(String username) {
        return ALL_USERS.get(username);
    }

    public static BasicAuthenticationSession getBySessionId(String sessionId) {
        try {
            return ALL_USERS.get(SESSION_IDS.getOrDefault(sessionId, ""));
        } catch (Exception e) {
            return null;
        }
    }
}
