package com.mm.qbot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static Long dateToStamp(String time)   throws ParseException{
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 Date date = new Date();
                try{
                         date = sf.parse(time);
                    } catch(ParseException e) {
                       e.printStackTrace();
                     }
                 return date.getTime();
    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(Long time){
        Date d = new Date(time);
                 sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sf.format(d);
    }
}
