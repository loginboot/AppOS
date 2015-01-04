package com.xsw.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsw.constant.Constant;
import com.xsw.ctx.MenuCtx;
import com.xsw.mapper.JsonMapper;
import com.xsw.model.Menu;
import com.xsw.model.Params;
import com.xsw.model.User;

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
     * 日志
     */
    private final static Logger log = Logger.getLogger(Util.class);

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
     * 时间戳转换为字符串
     * 
     * @param time Date
     * @return yyyy-MM-dd HH:mm:ss.SSSSSS
     */
    public static String timeStampToStr() {
        Date time = new Date();
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
     * Convert byte[] to hex
     * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * 
     * @param src byte[] data
     * 
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     * 
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     * 
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 生成压缩后的search parameters
     * 
     * @param request
     * @param map
     * @param prefix
     */
    public static void storeSearchKeyValue(HttpServletRequest request, Map<String, Object> map, String prefix) {
        String pfix = Util.trim(prefix);
        Map<String, Object> nmap = new HashMap<String, Object>();
        if (Util.isEmpty(pfix)) {
            for (String key : map.keySet()) {
                nmap.put(key, map.get(key));
            }
        } else {
            for (String key : map.keySet()) {
                if ("page".equals(key) || "pageSize".equals(key) || "rows".equals(key)) {
                    nmap.put(key, map.get(key));
                } else {
                    nmap.put(pfix + key, map.get(key));
                }
            }
        }
        String contextPath = request.getContextPath();
        String absolutePath = request.getRequestURI();
        String relativePath = absolutePath.substring(contextPath.length() + 1);// 获取相对路径
        getShiroSession().setAttribute(Constant.SEARCH_KEY + "_" + relativePath, ZipUtil.zipobj(nmap));
    }

    /**
     * 获取当前操作用户
     * 
     * @return User
     */
    public static User getCurrentUser() {
        Subject currentUser = SecurityUtils.getSubject();
        return (User) currentUser.getPrincipal();
    }

    /**
     * 获取当前操作Session
     * 
     * @return User
     */
    public static Session getShiroSession() {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.getSession();
    }

    /**
     * 将List<Menu>转换为有层次结构的List<MenuCtx>
     * 
     * @param menus
     * @return
     */
    public static List<MenuCtx> convertMenusToMenuCtxs(List<Menu> menus) {
        HashMap<Integer, MenuCtx> maps = new HashMap<Integer, MenuCtx>();
        // 将Menu解析为层次结构
        for (Menu me : menus) {
            MenuCtx mc = new MenuCtx(me);// 生成MenuCtx
            if (me.getMid() != me.getPid())// 非父节点
            {
                MenuCtx pmc = maps.get(me.getPid());// 获取父节
                if (pmc != null)// 如果父节点为空，则子节点也无权限
                {
                    mc.setParent(pmc);
                    pmc.addChildMenuCtx(mc);
                    maps.put(me.getMid(), mc);
                }
            } else
            // 父节点
            {
                maps.put(me.getMid(), mc);
            }
        }
        // 将root菜单节点返回
        List<MenuCtx> lst = new LinkedList<MenuCtx>();
        for (Menu me : menus) {
            if (me.getDepth() == 0)// root结点
            {
                lst.add(maps.get(me.getMid()));
            }
        }
        return lst;
    }

    /**
     * 获取指定参数和名称的参数值
     * 
     * @param params
     * @param paramName
     * @return
     */
    public static String getAppParamValue(List<Params> params, String paramName) {
        for (Params p : params) {
            if (paramName.equals(p.getName())) {
                return p.getValue();
            }
        }
        return "";
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
     * 以字符串形式返回异常堆栈信息
     * 
     * @param e
     * @return 异常堆栈信息字符串
     */
    public static String getStackTrace(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer, true));
        return writer.toString();
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
     * Json格式错误信息输出
     * 
     * @param ms ResourceBundleMessageSource
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param errorcode 错误代码
     * @param extendMsg 补充信息
     * @throws IOException
     */
    public static void writeJsonErrorMsg(ReloadableResourceBundleMessageSource ms, HttpServletRequest request,
            HttpServletResponse response, String errorcode, String extendMsg) throws IOException {
        HashMap<String, String> retmsg = new HashMap<String, String>();
        retmsg.put("CODE", errorcode);
        retmsg.put("MSG", Util.getMessageByCode(ms, request, errorcode) + "[" + extendMsg + "]");
        if (!"ERRCODE.1011".equals(errorcode)) {
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

    /**
     * 将Valid的错误信息与JSON格式返回到终端
     * 
     * @param ms
     * @param request
     * @param response
     * @param result
     * @return
     */
    public static Object writeJsonValidErrorMsg(ReloadableResourceBundleMessageSource ms, HttpServletRequest request,
            HttpServletResponse response, BindingResult result) {
        HashMap<String, Object> retmsg = new HashMap<String, Object>();
        retmsg.put("CODE", "ERRCODE.9000");// Validation Error
        retmsg.put("MSG", Util.getMessageByCode(ms, request, "ERRCODE.9000"));
        // reallocate the
        retmsg.put(Constant.UUID_TOKEN, uuid2());
        // Global Error
        Vector<String> gErrs = new Vector<String>();
        if (result.getGlobalErrorCount() > 0) {
            for (ObjectError err : result.getGlobalErrors()) {
                gErrs.add(Util.getMessageByCode(ms, request, err.getDefaultMessage()));
            }
        }
        retmsg.put("GLOBAL", gErrs);
        // Fields Error
        retmsg.put("FIELDERRS", result.getFieldErrors());

        JsonMapper mapper = new JsonMapper();
        try {
            return mapper.fromJson("{\"retmsg\":" + mapper.toJson(retmsg) + "}", Object.class);
        } catch (Exception e) {
            e.printStackTrace();
            return mapper.fromJson(
                    "{\"retmsg\":{\"MSG\":\"convert object to json failed\",\"CODE\":\"MSGCODE.9999\"}}", Object.class);
        }
    }

    /**
     * 删除指定目录中超过指定天数的文件
     * 
     * @param dir 要操作的目录
     * @param days 要删除文件超过指定天数
     */
    public static void deleteFileByDays(String dir, int days) {
        Calendar cal = Calendar.getInstance();
        File file = new File(dir);
        if (file.exists())// 首先判断是否存在
        {
            if (file.isDirectory())// 是目录
            {
                File[] list = file.listFiles();
                for (File sf : list) {
                    deleteFileByDays(sf.getAbsolutePath(), days);
                }
            } else if (file.isFile())// 是文件
            {
                cal.setTimeInMillis(file.lastModified());
                cal.add(Calendar.DATE, days);
                if (cal.getTime().compareTo(new Date()) < 0) {
                    log.debug("Delete file:" + file.getAbsolutePath() + "--" + file.delete());
                }
            }
        }
    }
}
