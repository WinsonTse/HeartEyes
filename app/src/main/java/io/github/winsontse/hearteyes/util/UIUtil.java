package io.github.winsontse.hearteyes.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by winson on 16/5/20.
 */
public class UIUtil {

    private static int deviceWidth;
    private static int deviceHeight;

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getDisplayMetrics(context));
    }


    public static int spToPx(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getDisplayMetrics(context));
    }

    public static int getDeviceWidth(Context context) {
        if (deviceWidth == 0) {
            deviceWidth = getDisplayMetrics(context).widthPixels;
        }
        return deviceWidth;
    }

    public static int getDeviceHeight(Context context) {
        if (deviceHeight == 0) {
            deviceHeight = getDisplayMetrics(context).heightPixels;
        }
        return deviceHeight;
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }
}
