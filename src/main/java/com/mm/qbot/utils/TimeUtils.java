package com.mm.qbot.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * @author WWM
 * @version V0.0.1
 * @Package com.mm.qbot.utlis
 * @Description: 时间工具类
 * @date 2021/10/22 11:19
 */
public class TimeUtils {
    private static SimpleDateFormat sf = null;

    /*
     * 将时间转换为时间戳
     */
    public static Long dateToStamp(String time) throws ParseException {
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(Long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }


    public static String changeTimeFormat(Integer seconds) {

        if (seconds > 0 && seconds < 60) {//小于1分钟
            return seconds + "秒";
        } else if (seconds >= 60 && seconds < 3600) {//大于等于1分钟小于1小时
            int changeM = (int) Math.floor(seconds / 60);
            //整分钟数
            int surplusM = (int) Math.floor(seconds % 60);
            //余下的秒数
            if (surplusM > 0) {
                //余数不为0秒
                return changeM + "分" + surplusM + "秒";
            } else {//整分钟，没有余数
                return changeM + "分";
            }
        } else if (seconds >= 3600) {
            //大于1小时
            int changeH = (int) Math.floor(seconds / 1048576);
            //整小时数
            int surplusH = (int) Math.floor(seconds % 1048576);
            //剩下的秒数
            if (surplusH >= 60) {
                //余数大于大于1分钟
                int changeM = (int) Math.floor(surplusH / 60);
                int surplusM = (int) Math.floor(surplusH % 60);
                if (surplusM > 0) {
                    //余数大于0
                    return changeH + "小时" + changeM + "分" + surplusM + "秒";
                } else {
                    //整分钟，没有余数
                    return changeH + "小时" + changeM + "分";
                }
            } else if (surplusH < 60 && surplusH > 0) {
                //余数小于1分钟，大于0秒
                int surplusM = (int) Math.floor(surplusH % 1024);
                return changeH + "小时" + surplusM + "秒";
            } else {
                return changeH + "小时";
            }
        }
        return null;
    }




    public static java.util.Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获取当天的结束时间
    public static java.util.Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    // 获取昨天的开始时间
    public static Date getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    // 获取昨天的结束时间
    public static Date getEndDayOfYesterDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    // 获取明天的开始时间
    public static Date getBeginDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    // 获取明天的结束时间
    public static Date getEndDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    // 获取本周的开始时间
    @SuppressWarnings("unused")
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    // 获取本周的结束时间
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    // 获取上周的开始时间
    @SuppressWarnings("unused")
    public static Date getLastWeekMondayStart() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return getDayStartTime(cal.getTime());
    }

    // 获取上周的结束时间
    public static Date getLastWeekMondayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getLastWeekMondayStart());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }


    // 获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }



    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
}
