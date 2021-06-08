package ir.iais.ui;

import ir.iais.metronic8.assets.Bootstrap5;
import ir.iais.metronic8.assets.js.MetronicJs;
import ir.iais.metronic8.assets.plugins.Plugins;
import ir.iais.ui.fonts.MetronicFontsCSS;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssUrlReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;

/**
 * @author vahid
 * create on 5/30/2021
 */
public class MetronicAssets {
    public static void render(IHeaderResponse response, Component component) {
        response.render(JavaScriptHeaderItem.forReference(component.getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
        response.render(CssUrlReferenceHeaderItem.forReference(MetronicFontsCSS.Fonts_CSS));
        response.render(JavaScriptReferenceHeaderItem.forReference(Bootstrap5.Bootstrap_JS));
        response.render(CssUrlReferenceHeaderItem.forReference(Plugins.Plugin_Metronic_Css));
        response.render(CssUrlReferenceHeaderItem.forReference(Bootstrap5.Bootstrap_CSS));
        response.render(JavaScriptReferenceHeaderItem.forReference(Plugins.Plugin_Metronic_Js));
        response.render(JavaScriptReferenceHeaderItem.forReference(MetronicJs.Script_Bundle_Js));
        response.render(JavaScriptReferenceHeaderItem.forReference(MetronicJs.Script_Widgets_Js));
        response.render(JavaScriptReferenceHeaderItem.forReference(MetronicJs.Script_Create_Js));
        response.render(JavaScriptReferenceHeaderItem.forReference(MetronicJs.Script_Upgrade_Js));
    }
}
