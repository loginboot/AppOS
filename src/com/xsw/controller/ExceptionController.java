package com.xsw.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.xsw.ascept.AppLog;
import com.xsw.exception.AppException;
import com.xsw.utils.Util;

@ControllerAdvice
public class ExceptionController {
    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(ExceptionController.class);

    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @AppLog(position = 0, details = "HandleException:")
    public ModelAndView handleException(Exception ex, HttpServletRequest request, HttpServletResponse resonpse) {
        String acceptStr = request.getHeader("accept");
        boolean isJson = false;
        // 判断是否json格式
        if (acceptStr != null && acceptStr.indexOf("application/json") >= 0) {
            isJson = true;
        }
        if (isJson) {
            try {
                if (ex instanceof AppException) {
                    Util.writeJsonErrorMsg(messageSource, request, resonpse, ex.getMessage());
                } else if (ex instanceof UnauthorizedException) {
                    Util.writeJsonErrorMsg(messageSource, request, resonpse, "ERRCODE.9997");
                } else if (ex instanceof UnauthenticatedException) {
                    Util.writeJsonErrorMsg(messageSource, request, resonpse, "ERRCODE.2001");
                } else if (ex instanceof InvalidDataAccessApiUsageException) {
                    Util.writeJsonErrorMsg(messageSource, request, resonpse, "ERRCODE.9991");
                } else {
                    log.error("App List Engine Error:", ex);
                    Util.writeJsonErrorMsg(messageSource, request, resonpse, "ERRCODE.9999", Util.getStackTrace(ex));
                }
            } catch (IOException e) {
                log.error("write error msg:", e);
            }

            return null;
        } else {
            ModelAndView mav = new ModelAndView("error/error");
            String retUrl = request.getParameter("returnUrl");
            if (Util.isEmpty(retUrl)) {
                retUrl = (String) request.getAttribute("returnUrl");
            }

            if (!Util.isEmpty(retUrl)) {
                mav.addObject("returnUrl", retUrl);
                // 均没有设置，如果是ERRCODE.2001则不处理,否则默认取上一步URL地址
            } else {
                if (!"ERRCODE.2001".equals(ex.getMessage())) {
                    String preUrl = request.getHeader("Referer");
                    if (!Util.isEmpty(preUrl)) {
                        String url = request.getRequestURL().toString();
                        String urlPre = url.substring(0, url.indexOf(request.getRequestURI()))
                                + request.getContextPath() + "/";
                        mav.addObject("returnUrl", preUrl.substring(urlPre.length()));
                    }
                }
            }

            // 返回出错信息
            if (ex instanceof AppException) {
                mav.addObject("errorMsg", Util.getMessageByCode(messageSource, request, ex.getMessage()));
            } else if (ex instanceof UnauthorizedException) {
                mav.addObject("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.9997"));
            } else if (ex instanceof UnauthenticatedException) {
                mav.addObject("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.2001"));
            } else if (ex instanceof InvalidDataAccessApiUsageException) {
                mav.addObject("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.9991"));
            } else {
                log.error("App List Engine Error:", ex);
                mav.addObject("errorMsg",
                        Util.getMessageByCode(messageSource, request, "ERRCODE.9999") + "[" + Util.getStackTrace(ex)
                                + "]");
            }

            return mav;
        } // end if JSON
    }
}
