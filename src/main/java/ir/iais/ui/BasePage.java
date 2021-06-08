package ir.iais.ui;

import ir.iais.metronic8.assets.Bootstrap5;
import ir.iais.ui.Side.MetronicSection;
import ir.iais.ui.Side.MetronicSidePanel;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.resource.PackageResourceReference;

import java.util.List;

/**
 * @author vahid
 * create on 5/30/2021
 */
public class BasePage extends WebPage implements IAjaxIndicatorAware {

    public BasePage(List<MetronicSection> sections) {
        Link<String> signOutLink = new Link<String>("signOutLink") {
            @Override
            public void onClick() {
                AuthenticatedWebSession.get().signOut();
                setResponsePage(getApplication().getHomePage());
            }
        };
        add(signOutLink);

        MetronicSidePanel metronicSidePanel = new MetronicSidePanel("metronicSidePanel") {
            @Override
            public PackageResourceReference getLogoImage() {
                return new PackageResourceReference(Bootstrap5.class, "media/logos/logo-1.svg");
            }

            @Override
            public List<MetronicSection> getSections() {
                return sections;
            }
        };
        add(metronicSidePanel);

        add(new Image("profilePhotoHeader", new PackageResourceReference(Bootstrap5.class, "media/avatars/blank.png")));
        add(new Image("profilePhotoPopUp", new PackageResourceReference(Bootstrap5.class, "media/avatars/blank.png")));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        MetronicAssets.render(response, this);
    }

    @Override
    public String getAjaxIndicatorMarkupId() {
        return null;
    }
}
