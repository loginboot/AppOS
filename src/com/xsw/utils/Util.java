package com.xsw.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Util {

    public static String trim(String val) {
        if (val == null) {
            return "";
        }
        return val.trim();
    }

    public static boolean isEmpty(String val) {
        if ("".equals(Util.trim(val))) {
            return true;
        }
        return false;
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid2() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 时间戳转换为字符串
     * 
     * @param time Date
     * @return yyyy-MM-dd HH:mm:ss.SSSSSS
     */
    public static String timeStampToStr(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return sdf.format(time);
    }

    /**
     * 日期格式转换
     * 
     * @param sdate 字符串日期
     * @param srcfmt 源日期格式
     * @param desfmt 目标日期格式
     * @return
     */
    public static String dateFormatChg(String sdate, String srcfmt, String desfmt) {
        String outdate = sdate;
        SimpleDateFormat sdf = new SimpleDateFormat(srcfmt, Locale.ENGLISH);
        SimpleDateFormat desf = new SimpleDateFormat(desfmt, Locale.ENGLISH);
        try {
            Date dt = sdf.parse(sdate);
            return desf.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outdate;
    }

    /**
     * 将dd-MMM-yyyy格式的日期转换为yyyy-MM-dd
     * 
     * @param sdate
     * @return
     */
    public static String ddMMMyyyyToyyyymmdd(String sdate) {
        return dateFormatChg(sdate, "dd-MMM-yyyy", "yyyy-MM-dd");
    }

    /**
     * 根据输入的年月日生成完整的日期字符串
     * 
     * @param day
     * @param month
     * @param year
     * @return
     */
    public static String getDateStr(String day, String month, String year) {
        String strdate = "";
        if (day != null) {
            strdate += day;
        }
        if (month != null) {
            if (!"".equals(strdate))// 已经有日期了
            {
                strdate = strdate + "-" + month;
            } else {
                strdate += month;
            }
        }
        if (year != null) {
            if (!"".equals(strdate))// 已经有日期了
            {
                strdate = strdate + "-" + year;
            } else {
                strdate += year;
            }
        }
        return strdate;
    }

    /**
     * 字符串转换为时间戳
     * 
     * @param times YYYY-MM-DD HH:mm:SS.ssssss
     * @return Date
     * @throws ParseException
     */
    public static Date strToTimeStamp(String times) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return sdf.parse(times);
    }

    /**
     * 日期转换为字符串
     * 
     * @param time Date
     * @return yyyy-MM-dd
     */
    public static String dateToStr(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    /**
     * 日期时间转换为字符串
     * 
     * @param time Date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String timeToStr(Date time) {
        return dateToStr(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 时间日期转换
     * 
     * @param datetime
     * @param pattern 默认"yyyy-MM-dd HH:mm"
     * @return
     */
    public static Date strToDateTime(String datetime, String pattern) throws ParseException {
        if (isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(datetime);
    }

    /**
     * 将日期转换为指定格式的字符串
     * 
     * @param date
     * @param pattern 默认为 "yyyy-MM-dd HH:mm"
     * @return String
     */
    public static String dateToStr(Date date, String pattern) {
        if (isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        return sdf.format(date);
    }

    /**
     * 字符串转换为日期
     * 
     * @param times YYYY-MM-DD
     * @return Date
     * @throws ParseException
     */
    public static Date strToDate(String times) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (Util.isEmpty(times)) {
            return sdf.parse("9999-12-31");
        } else {
            return sdf.parse(times);
        }

    }
}
