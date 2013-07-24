package org.ei.ziggy;

public class Context {
    private static Context context;

    private android.content.Context applicationContext;

    public static Context getInstance() {
        if (context == null) {
            context = new Context();
        }
        return context;
    }

    public void updateApplicationContext(android.content.Context applicationContext) {
        this.applicationContext = applicationContext;
    }
}
