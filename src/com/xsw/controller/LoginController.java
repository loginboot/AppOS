package com.xsw.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xsw.exception.AppException;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-12-28
 * @description 主系统登录
 *
 */
@Controller
public class LoginController extends BaseController {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(LoginController.class);

    /**
     * 跳转到用户登录界面
     * 
     * @param request
     * @param model
     * @return
     * @throws LyodsException
     */
    @RequestMapping(value = "/login.htm", method = RequestMethod.GET)
    public String login(HttpServletRequest request, ModelMap model) throws AppException {
        Subject currentUser = SecurityUtils.getSubject();

        return "system/login";
    }

    /**
     * 用户登录POST
     * 
     * @param request
     * @param username
     * @param password
     * @param rememberMe
     * @param userName
     * @param model
     * @param redirectAttributes
     * @return
     * @throws LyodsException
     * @throws ParseException
     */
    @RequestMapping(value = "/login.htm", method = RequestMethod.POST)
    public String login(HttpServletRequest request, @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
            @RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model,
            RedirectAttributes redirectAttributes) throws AppException, ParseException {

        return "redirect:/index.htm";
    }
}
