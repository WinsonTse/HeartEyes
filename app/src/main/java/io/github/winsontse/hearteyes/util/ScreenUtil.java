package io.github.winsontse.hearteyes.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import io.github.winsontse.hearteyes.R;

/**
 * Created by winson on 16/7/17.
 */

public class ScreenUtil {
    public static int screenWidth;
    public static int screenHeight;
    public static int statusBarHeight;
    public static int toolbarHeight;

    public static void init(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        statusBarHeight = getStatusBarHeight(resources);
        toolbarHeight = resources.getDimensionPixelOffset(R.dimen.toolbar);
    }

    private static int getStatusBarHeight(Resources resources) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return 0;
        }
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        if (result == 0) {
            result = resources.getDimensionPixelSize(R.dimen.status_bar_height_default);
        }
        return result;
    }
}
