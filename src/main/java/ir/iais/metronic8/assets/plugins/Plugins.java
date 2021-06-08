package ir.iais.metronic8.assets.plugins;

import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * @author vahid
 * create on 5/30/2021
 */
public class Plugins {

    public static final CssResourceReference Plugin_Metronic_Css = new CssResourceReference(Plugins.class, "global/plugins.bundle.rtl.css");
    public static final JavaScriptResourceReference Plugin_Metronic_Js = new JavaScriptResourceReference(Plugins.class, "global/plugins.bundle.js");

}
