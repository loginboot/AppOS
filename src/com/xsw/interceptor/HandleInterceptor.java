package com.xsw.interceptor;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.xsw.constant.Constant;
import com.xsw.model.User;
import com.xsw.service.LoginService;
import com.xsw.utils.Util;
import com.xsw.utils.ZipUtil;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-10-10
 * @description 系统权限拦截器 - 创建
 *
 */

public class HandleInterceptor extends HandlerInterceptorAdapter {
    private Logger log = Logger.getLogger(HandleInterceptor.class);

    @Autowired
    protected ReloadableResourceBundleMessageSource messageSource;
    @Autowired
    private LoginService loginService;

    /**
     * 处理前进行session检查
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("call preHandle...");
        String acceptStr = request.getHeader("accept");
        String contextPath = request.getContextPath();
        String absolutePath = request.getRequestURI();
        String relativePath = absolutePath.substring(contextPath.length() + 1); // 获取相对路径
        log.debug("--------interceptor----url:" + contextPath + "," + absolutePath + "," + relativePath
                + ",ContentType:" + request.getContentType() + ",accept:" + acceptStr);
        boolean isJson = false;
        // 判断是否json格式
        if (acceptStr != null && acceptStr.indexOf("application/json") >= 0) {
            isJson = true;
        }
        boolean needAuth = false;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequiresPermissions mthReqPer = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
            if (mthReqPer != null) {
                log.debug("handlerMethod*(" + handlerMethod + ")* has RequiresPermissions annotation.");
                needAuth = true;
            } else {
                RequiresAuthentication mthReqAuth = handlerMethod.getMethodAnnotation(RequiresAuthentication.class);
                if (mthReqAuth != null) {
                    log.debug("handlerMethod*(" + handlerMethod + ")* has RequiresAuthentication annotation.");
                    needAuth = true;
                } else {
                    RequiresAuthentication clsReqAuth = handlerMethod.getBeanType().getAnnotation(
                            RequiresAuthentication.class);
                    if (clsReqAuth != null) {
                        log.debug("handlerClass*(" + handlerMethod.getBean()
                                + ")* has RequiresAuthentication annotation.");
                        needAuth = true;
                    }
                }
            }
        }

        if (needAuth) {
            log.info("HandleInterceptor--handler:" + handler.toString());
            // check use password is init or not
            User currUser = Util.getCurrentUser();
            // 如果初始密码没有修改也要报错
            if (currUser != null && !"system/initPwd.do".equals(relativePath) && currUser.getInitPwd() == 1) {
                log.error("[" + currUser.getLoginName() + "]'s password didn't init.");
                if (isJson)// 判断是否json格式
                {
                    Util.writeJsonErrorMsg(messageSource, request, response, "ERRCODE.2003");
                } else
                // 非json
                {
                    request.setAttribute("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.2003"));
                    request.getRequestDispatcher("/index.htm").forward(request, response);
                }
                return false;
            }
            if (SecurityUtils.getSubject() == null || !SecurityUtils.getSubject().isAuthenticated()) {
                log.error("Didn't login please login first.");
                if (isJson)// 判断是否json格式
                {
                    Util.writeJsonErrorMsg(messageSource, request, response, "ERRCODE.2001");
                } else
                // 非json
                {
                    request.setAttribute("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.2001"));
                    request.getRequestDispatcher("/error.do").forward(request, response);
                }
                return false;
            }
            // check session
            if (loginService.check(currUser, currUser.getAppList(), (String) Util.getShiroSession().getId()) != 0) {
                Subject currentUser = SecurityUtils.getSubject();
                if (SecurityUtils.getSubject().getSession() != null) {
                    loginService.logoutSuccessful(Util.getCurrentUser().getUserId(), (String) Util.getShiroSession()
                            .getId());
                    currentUser.logout();
                }
                log.error("Check [" + currentUser + "] session failed.");
                // 判断是否json格式
                if (isJson) {
                    Util.writeJsonErrorMsg(messageSource, request, response, "ERRCODE.2001");
                    // 非json
                } else {
                    request.setAttribute("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.2001"));
                    request.getRequestDispatcher("/error.do").forward(request, response);
                }
                return false;
            }
        }
        // if there any search key
        String searchValue = (String) Util.getShiroSession().getAttribute(Constant.SEARCH_KEY + "_" + relativePath);
        if (!Util.isEmpty(searchValue)) {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> schMap = (HashMap<String, Object>) ZipUtil.unzipobj(searchValue);
            // set to attribute
            for (String keyname : schMap.keySet()) {
                Object keyval = schMap.get(keyname);
                if (keyval instanceof String[]) {
                    request.setAttribute(keyname, (String[]) keyval);
                } else {
                    request.setAttribute(keyname, String.valueOf(keyval));
                }
                log.debug("********SearchKeyName[" + keyname + "]'s Value is:" + keyval);
            }
        }
        // check if need to validate the random code
        String randomCode = request.getParameter(Constant.RANDOMCODEKEY);
        // need to check
        if (!Util.isEmpty(randomCode) && !relativePath.startsWith("error")) {
            // get session random code key
            String sessionCode = (String) request.getSession().getAttribute(Constant.RANDOMCODEKEY);
            if (!randomCode.equalsIgnoreCase(sessionCode))// inconsistent
            {
                // 判断是否json格式
                if (isJson) {
                    Util.writeJsonErrorMsg(messageSource, request, response, "ERRCODE.1026");
                    // 非json
                } else {
                    request.setAttribute("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.1026"));
                    request.getRequestDispatcher("/error.do").forward(request, response);
                }
                return false;
            } else {
                // remove session
                request.getSession().removeAttribute(Constant.RANDOMCODEKEY);
            }
        }
        // check if need prevent resubmit
        String token = request.getParameter(Constant.UUID_TOKEN);
        // need to prevent resubmit
        if (!Util.isEmpty(token) && !relativePath.startsWith("error")) {
            Subject currentUser = SecurityUtils.getSubject();
            @SuppressWarnings("unchecked")
            Vector<String> tokens = (Vector<String>) currentUser.getSession().getAttribute(Constant.UUID_TOKEN);
            if (tokens == null) {
                tokens = new Vector<String>();
                tokens.add(token);
            } else {
                // had submit
                if (tokens.contains(token)) {
                    log.warn("The token[" + token + "] had already exist, do not refresh.");
                    // 判断是否json格式
                    if (isJson) {
                        Util.writeJsonErrorMsg(messageSource, request, response, "ERRCODE.1011");
                        // 非json
                    } else {
                        request.setAttribute("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.1011"));
                        request.getRequestDispatcher("/error.do").forward(request, response);
                    }
                    return false;
                } else {
                    tokens.add(token);
                }
            }
            currentUser.getSession().setAttribute(Constant.UUID_TOKEN, tokens);
        }
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        return true;
    }

    /**
     * 完成请求
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {
        log.debug("call postHandle...");
        String contextPath = request.getContextPath();
        String absolutePath = request.getRequestURI();
        String relativePath = absolutePath.substring(contextPath.length() + 1);// 获取相对路径
        // if there any search key
        String searchValue = (String) Util.getShiroSession().getAttribute(Constant.SEARCH_KEY + "_" + relativePath);
        if (!Util.isEmpty(searchValue)) {
            log.info("found the search value:" + searchValue);
            @SuppressWarnings("unchecked")
            HashMap<String, Object> schMap = (HashMap<String, Object>) ZipUtil.unzipobj(searchValue);
            // set to attribute
            for (String keyname : schMap.keySet()) {
                Object keyval = schMap.get(keyname);
                if (keyval instanceof String[]) {
                    request.setAttribute(keyname, (String[]) keyval);
                } else {
                    request.setAttribute(keyname, String.valueOf(keyval));
                }
                log.debug("********SearchKeyName[" + keyname + "]'s Value is:" + keyval);
            }
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // log.debug("--------interceptor----afterCompletion:"+request.getContextPath()+","+request.getRequestURI());
    }
}
