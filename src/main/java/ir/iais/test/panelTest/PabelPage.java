package ir.iais.test.panelTest;

import ir.iais.test.BasicPage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;

/**
 * @author vahid
 * create on 6/7/2021
 */
//@AuthorizeInstantiation({"ADMIN"})
public class PabelPage extends BasicPage {

    public PabelPage() {
        super();
        add(new Label("salam", "salam"));
    }
}
