package org.ei.ziggy;

import android.content.Context;

public class ZiggyContext {
    private static ZiggyContext ziggyContext;

    private Context applicationContext;

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

    public Object ziggyFileLoader() {
        throw new RuntimeException("Not implemented.");
    }

    public Object formSubmissionRouter() {
        throw new RuntimeException("Not implemented.");
    }
}
