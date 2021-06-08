package ir.iais.ui;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.Link;

/**
 * @author vahid
 * create on 5/30/2021
 */
public class ProtectedLink extends Link<String> {


    private final Class<? extends Page> destinationClass;
    private final Page destinationPage;

    public ProtectedLink(String id, Class<? extends Page> destination) {
        super(id);
        this.destinationClass = destination;
        this.destinationPage = null;
        visibleDecide();
    }

    public ProtectedLink(String id, Page destination) {
        super(id);
        this.destinationClass = null;
        this.destinationPage = destination;
        visibleDecide();
    }

    private void visibleDecide() {
        setVisible(true); // todo vahid :
//        String[] roles = extractRoles();
//        if (roles == null) {
//            return;
//        }
//        try {
//            if (BasicAuthenticationSession.getLiveUser() != null) {
//                Set<String> userRoles = BasicAuthenticationSession.getLiveUser().getRoles();
//                boolean isAccessed = false;
//                for (String role : roles) {
//                    if (userRoles.contains(role)) {
//                        isAccessed = true;
//                        break;
//                    }
//                }
//                setVisible(isAccessed);
//            } else {
//                setVisible(roles.length == 0);
//            }
//        } catch (NullPointerException | UserClassException e) {
//            setVisible(roles.length == 0);
//        }
    }

    private String[] extractRoles() {
        AuthorizeInstantiation authorizeInstantiation;
        if (destinationClass != null) {
            authorizeInstantiation = this.destinationClass.getAnnotation(AuthorizeInstantiation.class);
        } else {
            authorizeInstantiation = this.destinationPage.getClass().getAnnotation(AuthorizeInstantiation.class);
        }

        if (authorizeInstantiation == null) {
            return null;
        }
        String[] roles = authorizeInstantiation.value();
        return roles;
    }

    @Override
    public void onClick() {
        setResponsePage(destinationClass);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(newAjaxEventBehavior("click"));
    }

    /**
     * @param event the name of the default event on which this link will listen
     *              to
     * @return the ajax behavior which will be executed when the user clicks the
     * link
     */
    protected AjaxEventBehavior newAjaxEventBehavior(String event) {
        return new AjaxEventBehavior(event) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onEvent(AjaxRequestTarget target) {
                onClick();
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);
            }

            @Override
            public boolean getStatelessHint(Component component) {
                return false;
            }
        };
    }

}
