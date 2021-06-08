package ir.iais.test;

import ir.iais.test.panelTest.LabelPage;
import ir.iais.test.panelTest.PabelPage;
import ir.iais.ui.BasePage;
import ir.iais.ui.Side.MetronicLinkPanel;
import ir.iais.ui.Side.MetronicRootAccordion;
import ir.iais.ui.Side.MetronicSection;
import ir.iais.ui.Side.MetronicSubMenuAccordion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author vahid
 * create on 6/6/2021
 */
public class BasicPage extends BasePage {

    private static List<MetronicSection> sections;

    public BasicPage() {
        super(allSections());
    }

    private static List<MetronicSection> allSections() {

        //link panel
        MetronicLinkPanel metronicLinkPanel1 = new MetronicLinkPanel("link1", LabelPage.class);
        MetronicLinkPanel metronicLinkPanel2 = new MetronicLinkPanel("link2", PabelPage.class);
//        MetronicLinkPanel metronicLinkPanel3 = new MetronicLinkPanel("link3");
//        MetronicLinkPanel metronicLinkPanel4 = new MetronicLinkPanel("link4");
//        MetronicLinkPanel metronicLinkPanel5 = new MetronicLinkPanel("link5");
//        MetronicLinkPanel metronicLinkPanel6 = new MetronicLinkPanel("link6");
//        MetronicLinkPanel metronicLinkPanel7 = new MetronicLinkPanel("link7");
//        MetronicLinkPanel metronicLinkPanel8 = new MetronicLinkPanel("link8");
//        MetronicLinkPanel metronicLinkPanel9 = new MetronicLinkPanel("link9");
//        MetronicLinkPanel metronicLinkPanel10 = new MetronicLinkPanel("link10");
//        MetronicLinkPanel metronicLinkPanel11 = new MetronicLinkPanel("link11");
//        MetronicLinkPanel metronicLinkPanel12 = new MetronicLinkPanel("link12");

        //sub menu
        MetronicSubMenuAccordion metronicSubMenuAccordion1 = new MetronicSubMenuAccordion("subMenu1", null, Arrays.asList(metronicLinkPanel1, metronicLinkPanel2));
//        MetronicSubMenuAccordion metronicSubMenuAccordion2 = new MetronicSubMenuAccordion("subMenu2", Collections.singletonList(metronicSubMenuAccordion1), Collections.singletonList(metronicLinkPanel2));
//        MetronicSubMenuAccordion metronicSubMenuAccordion3 = new MetronicSubMenuAccordion("subMenu3", null, Collections.singletonList(metronicLinkPanel3));
//
//        MetronicSubMenuAccordion metronicSubMenuAccordion4 = new MetronicSubMenuAccordion("subMenu4", Collections.singletonList(metronicSubMenuAccordion2), Collections.singletonList(metronicLinkPanel4));
//        MetronicSubMenuAccordion metronicSubMenuAccordion5 = new MetronicSubMenuAccordion("subMenu5", Collections.singletonList(metronicSubMenuAccordion2), Collections.singletonList(metronicLinkPanel5));
//        MetronicSubMenuAccordion metronicSubMenuAccordion6 = new MetronicSubMenuAccordion("subMenu6", Collections.singletonList(metronicSubMenuAccordion3), Collections.singletonList(metronicLinkPanel6));

        //menu
        MetronicRootAccordion metronicRootAccordion1 = new MetronicRootAccordion("menu1", Collections.singletonList(metronicSubMenuAccordion1), null);
//        MetronicRootAccordion metronicRootAccordion2 = new MetronicRootAccordion("menu2", Collections.singletonList(metronicSubMenuAccordion5), Collections.singletonList(metronicLinkPanel8));
//        MetronicRootAccordion metronicRootAccordion3 = new MetronicRootAccordion("menu3", Collections.singletonList(metronicSubMenuAccordion6), Collections.singletonList(metronicLinkPanel9));


        //sections
        MetronicSection metronicSection1 = new MetronicSection("section1", Arrays.asList(metronicRootAccordion1), null);
//        MetronicSection metronicSection2 = new MetronicSection("section2", Arrays.asList(metronicRootAccordion2, metronicRootAccordion3, metronicRootAccordion1), Collections.singletonList(metronicLinkPanel11));
//        MetronicSection metronicSection3 = new MetronicSection("section3", Arrays.asList(metronicRootAccordion3, metronicRootAccordion1, metronicRootAccordion2), Collections.singletonList(metronicLinkPanel12));
        return Collections.singletonList(metronicSection1);
    }
}
