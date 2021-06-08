package ir.iais.ui.Side;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;

/**
 * @author vahid
 * create on 5/31/2021
 */
public class MetronicSection extends Panel {

    private String sectionTitle;
    private List<MetronicRootAccordion> rootAccordions;

    private List<MetronicLinkPanel> panelLink;

    public MetronicSection(String sectionTitle, List<MetronicRootAccordion> rootAccordions, List<MetronicLinkPanel> panelLink) {
        super("metronicSection");
        this.sectionTitle = sectionTitle;
        this.rootAccordions = rootAccordions;
        this.panelLink = panelLink;
        add(new Label("sectionTitle", getSectionTitle()));
        ListView<MetronicRootAccordion> rootAccordionListView = new ListView<MetronicRootAccordion>("rootAccordionListView", getRootAccordions()) {
            @Override
            protected void populateItem(ListItem<MetronicRootAccordion> listItem) {
                MetronicRootAccordion metronicRootAccordion = listItem.getModelObject();
                listItem.add(new MetronicRootAccordion(metronicRootAccordion.getTitle(), metronicRootAccordion.getMetronicSubMenuAccordions(), metronicRootAccordion.getPanelLink()));
            }
        };
        add(rootAccordionListView);

        ListView<MetronicLinkPanel> panelLinkListView = new ListView<MetronicLinkPanel>("panelLinkListView", getPanelLink()) {
            @Override
            protected void populateItem(ListItem<MetronicLinkPanel> listItem) {
                MetronicLinkPanel metronicLinkPanel = listItem.getModelObject();
                listItem.add(new MetronicLinkPanel(metronicLinkPanel.getPanelName(), metronicLinkPanel.getDestinationClass()));
            }
        };
        add(panelLinkListView);
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public List<MetronicRootAccordion> getRootAccordions() {
        return rootAccordions;
    }

    public List<MetronicLinkPanel> getPanelLink() {
        return panelLink;
    }
}
