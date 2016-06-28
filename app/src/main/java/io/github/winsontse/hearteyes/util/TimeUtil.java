package io.github.winsontse.hearteyes.util;

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
    public static String parseTime(long createTime) {

        if (createTime == -1) {
            return "刚刚";
        }
        String result = "";
        String timeStr = "";

        long nowLongTime = System.currentTimeMillis();
        try {
            timeStr = new SimpleDateFormat("yyyy-MM-dd").format(createTime);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
        }

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
            result = timeStr;
        }
        return result;
    }

    public static int getDay(long time) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


}
