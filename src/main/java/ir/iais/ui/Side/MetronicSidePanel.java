package ir.iais.ui.Side;

import ir.iais.metronic8.assets.Bootstrap5;
import ir.iais.ui.MetronicAssets;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;

import java.util.List;

/**
 * @author vahid
 * create on 5/31/2021
 */
public abstract class MetronicSidePanel extends Panel {

    public MetronicSidePanel(String id) {
        super(id);

        //add desktop logo
        add(new Image("logoOrganization", getLogoImage() != null ? getLogoImage() : new PackageResourceReference(Bootstrap5.class, "media/logos/empty.svg")));

        //add sections
        ListView<MetronicSection> sectionListView = new ListView<MetronicSection>("sectionListView", getSections()) {
            @Override
            protected void populateItem(ListItem<MetronicSection> listItem) {
                MetronicSection metronicSection = listItem.getModelObject();
                listItem.add(new MetronicSection(metronicSection.getSectionTitle(), metronicSection.getRootAccordions(), metronicSection.getPanelLink()));
            }
        };
        add(sectionListView);
    }


    public abstract PackageResourceReference getLogoImage();

    public abstract List<MetronicSection> getSections();

    /**
     * render all css and js file for this panel (it's not optimize)
     *
     * @param response response
     */
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        MetronicAssets.render(response, this);
    }
}
