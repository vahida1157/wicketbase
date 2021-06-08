package ir.iais.ui.Side;

import ir.iais.authentication.BasicAuthenticationSession;
import ir.iais.domain.UserClassException;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.Set;

/**
 * @author vahid
 * create on 6/2/2021
 */
public class MetronicLinkPanel extends Panel {

    private String panelName;
    private final Class<? extends Page> destinationClass;
    private final Page destinationPage;

    public MetronicLinkPanel(String panelName, Class<? extends Page> destinationClass) {
        super("metronicLinkPanel");
        this.panelName = panelName;
        this.destinationClass = destinationClass;
        this.destinationPage = null;
        Link<String> link = new Link<String>("link", new Model<>()) {
            @Override
            public void onClick() {
                setResponsePage(getDestinationClass());
            }
//
//            @Override
//            protected void onInitialize() {
//                super.onInitialize();
//                add(new AjaxEventBehavior("click") {
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    protected void onEvent(AjaxRequestTarget target) {
//                        onClick();
//                    }
//
//                    @Override
//                    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
//                        super.updateAjaxAttributes(attributes);
//                    }
//
//                    @Override
//                    public boolean getStatelessHint(Component component) {
//                        return false;
//                    }
//                });
//            }
        };
        link.add(new Label("linkPanelName", getPanelName()));
        add(link);
//        visibleDecide();
    }

    private void visibleDecide() {
        String[] roles = extractRoles();
        if (roles == null) {
            return;
        }
        try {
            if (BasicAuthenticationSession.getLiveUser() != null) {
                Set<String> userRoles = BasicAuthenticationSession.getLiveUser().getRoles();
                boolean isAccessed = false;
                for (String role : roles) {
                    if (userRoles.contains(role)) {
                        isAccessed = true;
                        break;
                    }
                }
                setVisible(isAccessed);
            } else {
                setVisible(roles.length == 0);
            }
        } catch (NullPointerException | UserClassException e) {
            setVisible(roles.length == 0);
        }
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
        return authorizeInstantiation.value();
    }

    public String getPanelName() {
        return panelName;
    }

    public Class<? extends Page> getDestinationClass() {
        return destinationClass != null ? destinationClass : destinationPage.getClass();
    }

}
