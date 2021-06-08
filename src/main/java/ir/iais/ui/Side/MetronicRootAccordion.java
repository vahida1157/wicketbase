package ir.iais.ui.Side;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;

/**
 * @author vahid
 * create on 5/30/2021
 */
public class MetronicRootAccordion extends Panel {

    private final String title;

    private List<MetronicSubMenuAccordion> metronicSubMenuAccordion;
    private List<MetronicLinkPanel> panelLink;

    public MetronicRootAccordion(String title, List<MetronicSubMenuAccordion> metronicSubMenuAccordion, List<MetronicLinkPanel> panelLink) {
        super("rootAccordion");
        this.title = title;
        this.metronicSubMenuAccordion = metronicSubMenuAccordion;
        this.panelLink = panelLink;

        add(new Label("menuTitle", getTitle()));

        add(new ListView<MetronicSubMenuAccordion>("metronicSubMenuListView", getMetronicSubMenuAccordions()) {
            @Override
            protected void populateItem(ListItem<MetronicSubMenuAccordion> listItem) {
                MetronicSubMenuAccordion metronicSubMenuAccordion = listItem.getModelObject();
                listItem.add(new MetronicSubMenuAccordion(metronicSubMenuAccordion.getTitle(), metronicSubMenuAccordion.getMetronicSubMenuAccordion(), metronicSubMenuAccordion.getPanelLink()));
            }
        });

        ListView<MetronicLinkPanel> panelLinkListView = new ListView<MetronicLinkPanel>("panelLinkListView", getPanelLink()) {
            @Override
            protected void populateItem(ListItem<MetronicLinkPanel> listItem) {
                MetronicLinkPanel metronicLinkPanel = listItem.getModelObject();
                listItem.add(new MetronicLinkPanel(metronicLinkPanel.getPanelName(), metronicLinkPanel.getDestinationClass()));
            }
        };
        add(panelLinkListView);
    }

    public String getTitle() {
        return this.title;
    }

    public List<MetronicSubMenuAccordion> getMetronicSubMenuAccordions() {
        return metronicSubMenuAccordion;
    }

    public List<MetronicLinkPanel> getPanelLink() {
        return panelLink;
    }
}
