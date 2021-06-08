package ir.iais.authentication;

import ir.iais.authentication.sso.SSOProperties;
import ir.iais.domain.UserClass;
import ir.iais.needToDelete.RolePropLoader;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;

import java.util.HashSet;

/**
 * @author vahid
 * create on 6/6/2021
 */
public class BasicAuthenticationSession extends AuthenticatedWebSession {

    private UserClass user;
    private Roles roles;

    public BasicAuthenticationSession(Request request) {
        super(request);
    }

    @Override
    public boolean authenticate(String username, String password) {

        //user is authenticate if both username and password are valid
        user = UserClass.loadByUsername(username, password);
        //user.isEnabled() == null for migration to enable user
        final boolean isLogin = (!(user == null)) && (user.isEnabled() == null || user.isEnabled());
        if (isLogin) {
            roles = new Roles();
            Boolean smsVerify = RolePropLoader.getInstance().getSmsVerify();
            if (SSOProperties.getInstance().getStatus().equals(1) ||
                    (!user.getForceChangePassWord() && (!smsVerify || "admin".equals(user.getUsername())))) {
                roles.addAll(user.getRoles());
            } else {
                roles.add("PRE_LOGIN");
            }

            BasicAuthenticationSession get = ActiveUsers.get(username);
            if (get != null) {
                get.invalidateNow();
            }
            ActiveUsers.add(username, this);
        }
        return isLogin;
    }

    @Override
    public Roles getRoles() {
        if (isSignedIn()) {
            return roles;
        }
        return null;
    }

    @Override
    public void invalidate() {
        if (user != null) {
            ActiveUsers.remove(user.getUsername());
        }
        super.invalidate();
    }

    @Override
    public void signOut() {
        if (user != null) {
            ActiveUsers.remove(user.getUsername());
        }
        super.signOut();
    }

    public UserClass getUser() {
        return user;
    }

    public String getSessionId() {
        return this.getSessionStore().getSessionId(RequestCycle.get().getRequest(), true);
    }

    public static UserClass getLiveUser() {
        try {
            UserClass uc = ((BasicAuthenticationSession) BasicAuthenticationSession.get()).getUser();
            if (!uc.isPersisted()) {
                uc = UserClass.loadByUsernameAndCodeMelli(uc.getUsername(), uc.getMeliCode());
            }
            return uc;
        } catch (Exception e) {
            return new UserClass("", "", "", "", "", "", new HashSet<>(), new HashSet<>());
        }
    }
}
