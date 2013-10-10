package org.ei.ziggy.util;

import static android.util.Log.*;
import static android.util.Log.e;

public class Log {
    public static void logVerbose(String message) {
        v("ZIGGY", message);
    }

    public static void logInfo(String message) {
        i("ZIGGY", message);
    }

    public static void logDebug(String message) {
        d("ZIGGY", message);
    }

    public static void logWarn(String message) {
        w("ZIGGY", message);
    }

    public static void logError(String message) {
        e("ZIGGY", message);
    }
}
