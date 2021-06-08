package ir.iais.metronic8.assets.js;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * @author vahid
 * create on 5/30/2021
 */
public class MetronicJs {

    public static final JavaScriptResourceReference Script_Bundle_Js = new JavaScriptResourceReference(MetronicJs.class, "scripts.bundle.js");
    public static final JavaScriptResourceReference Script_Widgets_Js = new JavaScriptResourceReference(MetronicJs.class, "custom/widgets.js");
    public static final JavaScriptResourceReference Script_Create_Js = new JavaScriptResourceReference(MetronicJs.class, "custom/modals/create-app.js");
    public static final JavaScriptResourceReference Script_Upgrade_Js = new JavaScriptResourceReference(MetronicJs.class, "custom/modals/upgrade-plan.js");

}
