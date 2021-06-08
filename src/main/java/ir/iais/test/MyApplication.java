package ir.iais.test;

import ir.iais.test.panelTest.LabelPage;
import ir.iais.test.panelTest.PabelPage;
import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * @author vahid
 * create on 5/30/2021
 */
public class MyApplication extends WebApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return PabelPage.class;
    }

    @Override
    protected void init() {
        super.init();
        getMarkupSettings().setStripWicketTags(true);

        getPageSettings().setVersionPagesByDefault(true);
        getRequestCycleListeners().add(new AbstractRequestCycleListener() {
            @Override
            public IRequestHandler onException(RequestCycle cycle, Exception ex) {
                Logger.getLogger(getClass()).error(ex.getMessage(), ex);
                return super.onException(cycle, ex);
            }

        });
    }

}
