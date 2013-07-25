package org.ei.ziggy;

import android.content.Context;
import org.ei.ziggy.service.ZiggyFileLoader;

public class ZiggyContext {
    private static ZiggyContext ziggyContext;

    private Context applicationContext;

    private ZiggyFileLoader ziggyFileLoader;

    public static ZiggyContext getInstance() {
        if (ziggyContext == null) {
            ziggyContext = new ZiggyContext();
        }
        return ziggyContext;
    }

    public void updateApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object formDataRepository() {
        throw new RuntimeException("Not implemented.");
    }

    public ZiggyFileLoader ziggyFileLoader() {
        if (ziggyFileLoader == null) {
            ziggyFileLoader = new ZiggyFileLoader("www/ziggy", "www/form", applicationContext.getAssets());
        }
        return ziggyFileLoader;
    }

    public Object formSubmissionRouter() {
        throw new RuntimeException("Not implemented.");
    }
}
