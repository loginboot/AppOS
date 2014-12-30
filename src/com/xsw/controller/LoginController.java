package com.xsw.controller;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
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

import com.xsw.constant.Constant;
import com.xsw.ctx.AppListCtx;
import com.xsw.exception.AppException;
import com.xsw.model.AppList;
import com.xsw.model.Params;
import com.xsw.security.AppAuthToken;
import com.xsw.service.LoginService;

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

    @Resource
    private LoginService loginService;

    /**
     * 跳转到用户登录界面
     * 
     * @param request
     * @param model
     * @return
     * @throws AppException
     */
    @RequestMapping(value = "/login.htm", method = RequestMethod.GET)
    public String login(HttpServletRequest request, ModelMap model) throws AppException {
        Subject currentUser = SecurityUtils.getSubject();

        // 应用系统列表
        List<AppList> applst = loginService.findAllApp();
        model.addAttribute("applst", applst);

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
     * @return
     */
    @RequestMapping(value = "/login.htm", method = RequestMethod.POST)
    public String login(HttpServletRequest request, @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
            @RequestParam(value = "appId", required = true) int appId,
            @RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model)
            throws AppException, ParseException {
        // 用户登录实现
        Subject currentUser = SecurityUtils.getSubject();
        log.info(username + " *** login post *** isRemembered:" + rememberMe);
        //logout first to update the SessionID
        if (currentUser != null) {
            currentUser.logout();
        }

        //应用查询
        AppList app = loginService.findByAppId(appId);
        List<Params> plst = loginService.getAppParams(appId);
        AppListCtx appCtx = new AppListCtx();
        appCtx.setAppList(app);
        appCtx.setParams(plst);
        request.getSession().setAttribute(Constant.SESSION_APP_LIST, appCtx);

        log.info(username + " *** login post *** isRemembered:" + rememberMe);
        AppAuthToken token = new AppAuthToken(app.getAppId(), username, password, rememberMe);
        boolean isOk = false;
        try {
            currentUser.login(token);
            isOk = true;
        } catch (Exception e) {

        }

        return "redirect:/" + app.getUrlName();
    }
}
