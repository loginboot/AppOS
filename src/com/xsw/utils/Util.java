package com.xsw.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsw.constant.Constant;
import com.xsw.mapper.JsonMapper;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-12-27
 * @description 系统工具类
 *
 */

public class Util {

    /**
     * 字符串去空格处理
     * @param val
     * @return
     */
    public static String trim(String val) {
        if (val == null) {
            return "";
        }
        return val.trim();
    }

    /**
     * 判断字符串是否为空 
     * @param val
     * @return
     */
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
     * 函数功能：如果源字符串的长度(不包括符号)小于指定长度，则在前面补零，使之达到指定长度
     * 
     * @param accno 输入字符串
     * @param strLen 需要输出字符串长度，不包括符号，
     * @return 补零后的字符串
     *         <p>
     *         <blockquote>
     * 
     *         <pre>
     * 例如：
     *      leftFillZero(&quot;-1&quot;,3) 输出为-001，
     *      leftFillZero(&quot;1&quot;,3) 输出为001
     *      leftFillZero(&quot;&quot;,4)  输出0000
     * leftFillZero(null,4) 输出null
     * 
     *         </p>
     *         </blockquote></pre>
     */
    public static String leftFillZero(String accno, int strLen) {
        if (accno == null) {
            return null;
        }
        int tempLen = accno.length();
        StringBuffer retVal = new StringBuffer(accno);
        if (tempLen == 0) {
            for (int i = 0; i < strLen; i++) {
                retVal.insert(0, "0");
            }
            return retVal.toString();
        }
        if (accno.charAt(0) == '-') {
            if (tempLen > strLen) {
                return accno;
            }
            for (int i = 0; i <= (strLen - tempLen); i++) {
                retVal.insert(1, "0");
            }
        } else {
            if (tempLen >= strLen) {
                return accno;
            }
            for (int i = 0; i < (strLen - tempLen); i++) {
                retVal.insert(0, "0");
            }
        }
        return retVal.toString();
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

    /**
     * 初始化Kendo Grid表格的页码与每页Size
     * 
     * @param request
     */
    public static void initPage(HttpServletRequest request) {
        if (request.getAttribute("page") == null) {
            request.setAttribute("page", "1");
        }
        if (request.getAttribute("pageSize") == null) {
            request.setAttribute("pageSize", Constant.DEFAULT_PAGE_SIZE);
        }
    }

    /**
     * 根据retmsg的错误代码和错误描述组装成json内容
     * 
     * @param retmsg HashMap
     * @return "retmsg":HashMap的json内容
     */
    public static String retmsg(HashMap<String, String> retmsg) {
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            return "\"retmsg\":" + jsonMapper.writeValueAsString(retmsg);
        } catch (Exception e) {
            return "\"retmsg\":{\"MSG\":\"convert object to json failed\",\"CODE\":\"MSGCODE.9999\"}";
        }
    }

    /**
     * 根据messageSource、cookie和code获取对应的多语言信息
     * 
     * @param ms ResourceBundleMessageSource
     * @param request HttpServletRequest
     * @param code String
     * @return code对应的描述
     */
    public static String getMessageByCode(ReloadableResourceBundleMessageSource ms, HttpServletRequest request,
            String code) {
        Locale locale = Locale.US;
        String lang = "en_US";
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (int i = 0; i < cks.length; i++) {
                if (cks[i].getName().indexOf(".LOCALE") > 0)// 获取当前语言
                {
                    lang = cks[i].getValue();
                }
            }
        }
        if ("zh_CN".equalsIgnoreCase(lang)) {
            locale = Locale.CHINA;
        } else if ("zh_TW".equalsIgnoreCase(lang)) {
            locale = Locale.TAIWAN;
        } else {
            locale = Locale.US;
        }
        return ms.getMessage(code, null, locale);
    }

    /**
     * Json格式错误信息输出
     * 
     * @param ms ResourceBundleMessageSource
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param errorcode 错误代码
     * @throws IOException
     */
    public static void writeJsonErrorMsg(ReloadableResourceBundleMessageSource ms, HttpServletRequest request,
            HttpServletResponse response, String errorcode) throws IOException {
        HashMap<String, String> retmsg = new HashMap<String, String>();
        retmsg.put("CODE", errorcode);
        retmsg.put("MSG", Util.getMessageByCode(ms, request, errorcode));
        if (!"ERRCODE.1011".equals(errorcode)) {
            // reallocate the UUID
            retmsg.put(Constant.UUID_TOKEN, uuid2());
        }
        response.setContentType("application/json;charset=utf-8");
        PrintWriter pw = response.getWriter();
        pw.write("{" + Util.retmsg(retmsg) + "}");
        pw.close();
    }

    /**
     * 输出成功消息
     * 
     * @param ms
     * @param request
     * @param response
     * @param msgcode 消息代码
     * @return
     * @throws IOException
     */
    public static Object writeJsonSuccMsg(ReloadableResourceBundleMessageSource ms, HttpServletRequest request,
            HttpServletResponse response, String msgcode) {
        HashMap<String, String> retmsg = new HashMap<String, String>();
        retmsg.put("CODE", msgcode);
        retmsg.put("MSG", Util.getMessageByCode(ms, request, msgcode));
        // reallocate the
        retmsg.put(Constant.UUID_TOKEN, uuid2());
        JsonMapper mapper = new JsonMapper();
        return mapper.fromJson("{" + Util.retmsg(retmsg) + "}", Object.class);
    }

    /**
     * 输出成功消息
     * 
     * @param ms
     * @param request
     * @param response
     * @param msgcode 消息代码
     * @param othermsg
     * @return
     * @throws IOException
     */
    public static Object writeJsonSuccMsg(ReloadableResourceBundleMessageSource ms, HttpServletRequest request,
            HttpServletResponse response, String msgcode, String othermsg) {
        HashMap<String, String> retmsg = new HashMap<String, String>();
        retmsg.put("CODE", msgcode);
        retmsg.put("MSG", Util.getMessageByCode(ms, request, msgcode));
        // reallocate the UUID
        retmsg.put(Constant.UUID_TOKEN, uuid2());
        JsonMapper mapper = new JsonMapper();
        return mapper.fromJson("{" + Util.retmsg(retmsg) + "," + othermsg + "}", Object.class);
    }

    /**
     * 分页Json数据返回
     * 
     * @param ret
     * @return json object
     */
    public static Object writePagableJson(Page<?> ret) {
        JsonMapper mapper = new JsonMapper();
        return mapper.fromJson("{\"total\":" + ret.getTotalElements() + ",\"rows\":" + mapper.toJson(ret.getContent())
                + "}", Object.class);
    }

    /**
     * 分布Json数据输出带参数
     * 
     * @param ret
     * @param othermsg
     * @return
     */
    public static Object writePagableJson(Page<?> ret, String othermsg) {
        JsonMapper mapper = new JsonMapper();
        return mapper.fromJson("{\"total\":" + ret.getTotalElements() + ",\"rows\":" + mapper.toJson(ret.getContent())
                + "," + othermsg + "}", Object.class);
    }
}
