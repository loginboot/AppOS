package com.xsw.ascept;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.xsw.constant.Constant;
import com.xsw.dao.AtLogDao;
import com.xsw.exception.AppException;
import com.xsw.model.AtLog;
import com.xsw.model.User;
import com.xsw.utils.Util;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-12-30
 * @description 系统日志登记类
 *
 */

public class LogAspect {
    /**
     * 日 志
     */
    private static Logger log = Logger.getLogger(LogAspect.class);

    @Autowired
    protected ReloadableResourceBundleMessageSource messageSource;

    @Resource
    private AtLogDao atLogDao;

    /**
     * 日志拦截器,方法前执行
     * @param jp
     */
    public void beforeHandler(JoinPoint jp) {
        log.debug("------------aspect--------beforeHandler>>" + jp.getTarget().getClass() + "," + jp.getKind() + ","
                + jp.toLongString());
        MethodSignature joinPointObject = (MethodSignature) jp.getSignature();
        RequiresPermissions mthReqPer = joinPointObject.getMethod().getAnnotation(RequiresPermissions.class);
        if (mthReqPer == null) {
            return;
        }
        for (Object arg : jp.getArgs()) {
            if (arg instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) arg;
                String permissions = null;
                if (mthReqPer != null) {
                    String[] rps = mthReqPer.value();
                    permissions = rps[0];
                }
                request.setAttribute(Constant.HANDLE_EXCEPTION, permissions);
                break;
            }
        }
    }

    /**
     * 系统日志,方法后执行登记日志
     * @param jp
     * @param retval
     */
    @SuppressWarnings("unchecked")
    public void afterHandler(JoinPoint jp, Object retval) {
        log.debug("------------aspect--------afterHandler>>" + jp.toLongString());
        MethodSignature joinPointObject = (MethodSignature) jp.getSignature();
        RequiresPermissions mthReqPer = joinPointObject.getMethod().getAnnotation(RequiresPermissions.class);
        AppLog aplog = joinPointObject.getMethod().getAnnotation(AppLog.class);
        log.debug(String.format(">>JoinPoint Method %s, RequiresPermissions:%s, AppLog:%s.",
                joinPointObject.getMethod(), mthReqPer, aplog));
        // 登记有注解的记录
        if (aplog != null) {
            Pattern pt = Pattern
                    .compile("execution\\(.*?Controller\\.(add.*|insert.*|del.*|delete.*|upd.*|modify.*|update.*|reset.*|login.*|logout.*|handleException).*\\)");
            Matcher mt = pt.matcher(jp.toShortString()); // 过滤方法名称
            if (!mt.find()) {
                log.warn(String.format("find LyodsLog annotation but can't match Controller method for (%s).",
                        jp.toShortString()));
                return;
            }
            // get parameter value
            String actVal = "";
            String permissions = null;
            if (mthReqPer != null) {
                String[] rps = mthReqPer.value();
                permissions = rps[0];
            }
            int type = 4;// 0.查询 1.新增 2.修改 3.删除 4.其他
            String strop = mt.group(1);
            if (strop.startsWith("add") || strop.startsWith("insert")) {
                type = 1;
            } else if (strop.startsWith("del") || strop.startsWith("delete")) {
                type = 3;
            } else if (strop.startsWith("update") || strop.startsWith("modify") || strop.startsWith("upd")
                    || strop.startsWith("reset")) {
                type = 2;
            } else if (strop.startsWith("login") || strop.startsWith("logout")) {
                permissions = "system";
            } else if (strop.startsWith("handleException")) {
                permissions = "exception";
            }
            log.debug("Permissions is " + permissions);

            int pos = aplog.position();
            String nameStr = aplog.name();
            String details = aplog.details();
            if (!Util.isEmpty(permissions)) {
                String[] pers = permissions.split("-");
                if (pers.length >= 2) {
                    permissions = pers[0] + "-" + pers[1];
                }
                if (pos != -1) {
                    if (!Util.isEmpty(nameStr)) {
                        Object argbean = jp.getArgs()[pos];
                        BeanWrapper bwp = new BeanWrapperImpl(argbean);
                        String[] names = Util.trim(nameStr).split(",|;");
                        for (String name : names) {
                            if (Util.isEmpty(actVal)) {
                                actVal = String.valueOf(bwp.getPropertyValue(name));
                            } else {
                                actVal += ";" + String.valueOf(bwp.getPropertyValue(name));
                            }
                        }
                    } else {
                        actVal = String.valueOf(jp.getArgs()[pos]);
                    }
                }
            } // 判斷權限并取得權限的應對參數值

            if (!Util.isEmpty(details)) {
                details = String.format(details, jp.getArgs());
                actVal = Util.trim(details) + " " + Util.trim(actVal);
            }

            // 参数与保存到日志中
            StringBuffer strArgs = new StringBuffer();
            for (Object arg : jp.getArgs()) {
                strArgs.append(arg);
                strArgs.append(",");
            }

            for (Object arg : jp.getArgs()) {
                // 日志登记
                if (arg instanceof HttpServletRequest) {
                    HttpServletRequest request = (HttpServletRequest) arg;
                    String acceptStr = request.getHeader("accept");
                    boolean isJson = false;
                    // 判断是否json格式
                    if (acceptStr != null && acceptStr.indexOf("application/json") >= 0) {
                        isJson = true;
                    }
                    Subject currentUser = SecurityUtils.getSubject();
                    User user = (User) currentUser.getPrincipal();

                    AtLog alog = new AtLog();
                    alog.setOperTime(Util.timeStampToStr(new Date()));
                    if (user != null) {
                        alog.setAppList(user.getAppList());
                    }
                    alog.setUser(user);
                    alog.setType(type);
                    alog.setRemoteHost(request.getRemoteAddr());
                    alog.setPermission(permissions);
                    if (Util.isEmpty(actVal)) {
                        alog.setDetails(jp.toLongString() + ",args:" + strArgs + "retVal:" + retval);
                    } else {
                        String retStr = "";
                        if (strop.startsWith("handleException")) {
                            if (!Util.isEmpty((String) request.getAttribute(Constant.HANDLE_EXCEPTION))) {
                                permissions = (String) request.getAttribute(Constant.HANDLE_EXCEPTION);
                                String doAction = "";
                                String[] pers = permissions.split("-");
                                if (pers.length >= 2) {
                                    permissions = pers[0] + "-" + pers[1];
                                    if (pers.length > 2) {
                                        doAction = pers[2];
                                    }
                                }
                                if (doAction.startsWith("add")) {
                                    type = 1;
                                } else if (doAction.startsWith("upd")) {
                                    type = 2;
                                } else if (doAction.startsWith("del")) {
                                    type = 3;
                                }
                                alog.setType(type);
                                alog.setPermission(permissions);
                            }
                            Object ex = jp.getArgs()[pos];
                            if (ex instanceof AppException) {
                                actVal = "Exception: "
                                        + Util.getMessageByCode(messageSource, request,
                                                ((AppException) ex).getMessage());
                            }
                        } else {
                            retStr = retval.toString();
                            if (isJson) {
                                Map<String, Map<String, Object>> retmsg = (Map<String, Map<String, Object>>) retval;
                                retStr = retmsg.get("retmsg").get("MSG").toString();
                            } else {
                                String prefix = "";
                                if (strop.startsWith("logout")) {
                                    prefix = request.getAttribute("LOGIN_NAME") + " ";
                                }
                                if (retStr.indexOf("error/error") >= 0
                                        || (strop.startsWith("login") && retStr.startsWith("/login"))) {
                                    if (strop.startsWith("login")) {
                                        retStr = prefix
                                                + "failure ("
                                                + request
                                                        .getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME)
                                                + ")";
                                    } else {
                                        retStr = prefix + "failure (" + retStr + ")";
                                    }
                                } else {
                                    retStr = prefix + "successful";
                                }
                            }
                        }
                        alog.setDetails(actVal + " " + retStr);
                    }// end if

                    atLogDao.save(alog); // save
                    log.debug("------------aspect--------alog>>" + alog);
                } // end if
            } // end for

        }
    }
}
