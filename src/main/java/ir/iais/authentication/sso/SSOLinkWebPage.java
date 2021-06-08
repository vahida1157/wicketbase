/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.iais.authentication.sso;

import ir.iais.authentication.sso.css.PrimeCss;
import ir.iais.authentication.sso.js.PrimeJs;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * @author karam
 */
public class SSOLinkWebPage extends WebPage {

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(new CssResourceReference(PrimeCss.class, "sso.css")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(PrimeJs.class, "sso.js")));
        response.render(OnDomReadyHeaderItem.forScript("setUrl(\"" + SSOUtils.getInstance().getSSOLoginLink() + "\");"));
    }
}
