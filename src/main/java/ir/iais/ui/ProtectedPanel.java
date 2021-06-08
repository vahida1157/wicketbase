package ir.iais.ui;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author vahid
 * create on 5/30/2021
 */
public class ProtectedPanel extends Panel {

    private boolean decideVisible = false;

    public ProtectedPanel(String id) {
        super(id);
    }

    @Override
    public MarkupContainer add(Component... childs) {
        final MarkupContainer add = super.add(childs);

        for (Component component : add) {
            if (component instanceof ProtectedLink || component instanceof ProtectedContainer) {
                decideVisible = decideVisible || (component.isVisible() && (!(component instanceof ProtectedContainer) || (((ProtectedContainer) component).isDecideVisible())));
            }
        }

        return add;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisible(isVisible() && decideVisible);
    }

}
