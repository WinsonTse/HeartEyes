package io.github.winsontse.hearteyes.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by winson on 16/7/17.
 */

public class ScreenUtil {
    public static int screenWidth;
    public static int screenHeight;

    public static void init(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight =displayMetrics.heightPixels;
    }
}
