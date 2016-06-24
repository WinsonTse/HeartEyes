package io.github.winsontse.hearteyes.util;

import android.util.Log;

/**
 * Created by winson on 16/6/19.
 */
public class LogUtil {
    public static final String TAG = "winson";

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

}
