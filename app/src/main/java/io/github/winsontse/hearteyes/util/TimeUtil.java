package io.github.winsontse.hearteyes.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hao.xie on 16/5/10.
 */
public class TimeUtil {

    private static Calendar calendar;

    public static Calendar getCalendar() {
        if (calendar == null) {
            synchronized (TimeUtil.class) {
                if (calendar == null) {
                    calendar = Calendar.getInstance();
                }
            }
        }
        return calendar;
    }

    /**
     * 转换为多少时间前
     *
     * @param createTime
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String parseTime(long createTime) {

        if (createTime == -1) {
            return "刚刚";
        }
        String result = "";
        String timeStr = "";

        long nowLongTime = System.currentTimeMillis();


        long distanceSeconds = (nowLongTime - createTime) / 1000;

        if (distanceSeconds <= 60) {
            result = "刚刚";
        } else if (distanceSeconds <= 60 * 60) {
            result = distanceSeconds / 60 + "分钟前";
        } else if (distanceSeconds <= 24 * 60 * 60) {
            result = distanceSeconds / 60 / 60 + "小时前";
        } else if (distanceSeconds <= 7 * 24 * 60 * 60) {
            result = distanceSeconds / 60 / 60 / 24 + "天前";
        } else {
            timeStr = getFormatTime(createTime, "yyyy-MM-dd");
            result = timeStr;
        }
        return result;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFormatTime(long time, String format) {
        String result = "";
        try {
            result = new SimpleDateFormat(format).format(time);
        } catch (Exception e) {
            result = "";
            LogUtil.e("时间转换错误:" + e.getMessage());
        } finally {
            return result;
        }
    }

    public static int getDay(long time) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    public static String getWeek(long currentTime) {
        calendar.setTimeInMillis(currentTime);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
            default:
                return "";
        }
    }
}
