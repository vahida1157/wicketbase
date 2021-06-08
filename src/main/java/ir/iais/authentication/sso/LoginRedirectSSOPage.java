/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.iais.authentication.sso;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.pages.RedirectPage;

/**
 * @author Zahra
 */
public class LoginRedirectSSOPage extends WebPage {

    public LoginRedirectSSOPage() {
        setResponsePage(new RedirectPage(SSOProperties.getInstance().getSsoURL()));
    }
}
