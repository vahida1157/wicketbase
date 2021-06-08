package ir.iais.ui;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * @author vahid
 * create on 5/30/2021
 */
public class ProtectedContainer extends WebMarkupContainer {

    private boolean decideVisible = false;

    public ProtectedContainer(String id) {
        super(id);
    }

    @Override
    public MarkupContainer add(Component... children) {
        final MarkupContainer add = super.add(children);

        for (Component component : add) {
            if (component instanceof ProtectedLink || component instanceof ProtectedContainer) {
                decideVisible = decideVisible || (component.isVisible() && (!(component instanceof ProtectedContainer) || (((ProtectedContainer) component).decideVisible)));
            }
        }
        return add;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisible(isVisible() && decideVisible);
    }

    public boolean isDecideVisible() {
        return decideVisible;
    }

}
