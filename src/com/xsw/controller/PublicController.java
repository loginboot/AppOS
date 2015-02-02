package com.xsw.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.xsw.constant.Constant;
import com.xsw.ctx.AppListCtx;
import com.xsw.model.AppList;
import com.xsw.model.Params;
import com.xsw.service.LoginService;
import com.xsw.utils.Util;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-12-30
 * @description 系统主页面控制跳转类
 *
 */
@Controller
@SessionAttributes(Constant.SESSION_APP_LIST)
public class PublicController {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(PublicController.class);

    @Resource
    private LoginService loginService;

    /**
     * 总系统主页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/index.htm")
    public String index(HttpServletRequest request, Model model) {
        AppListCtx actx = (AppListCtx) request.getSession().getAttribute(Constant.SESSION_APP_LIST);
        if (actx == null || actx.getAppList() == null) {
            log.debug("loading system app list...");
            AppList app = loginService.findSystemApp();
            List<Params> params = loginService.getAppParams(app.getAppId());
            actx = new AppListCtx();
            actx.setAppList(app);
            actx.setParams(params);
            model.addAttribute(Constant.SESSION_APP_LIST, actx);
            request.getSession().setAttribute(Constant.SESSION_APP_LIST, actx);
        }
        log.info("index AppList:" + actx);

        // 密码安全等控制
        List<Params> params = loginService.getAppParams(actx.getAppList().getAppId());
        model.addAttribute(
                "PWD_MIN_LENGTH",
                Util.isEmpty(Util.getAppParamValue(params, Constant.PWD_MIN_LENGTH)) ? "-1" : Util.getAppParamValue(
                        params, Constant.PWD_MIN_LENGTH)); // 密码最小长度
        model.addAttribute(
                "PWD_MIN_ALPHA_CHAR",
                Util.isEmpty(Util.getAppParamValue(params, Constant.PWD_MIN_ALPHA_CHAR)) ? "-1" : Util
                        .getAppParamValue(params, Constant.PWD_MIN_ALPHA_CHAR)); // 最少字母个数
        model.addAttribute(
                "PWD_MIN_NUM_CHAR",
                Util.isEmpty(Util.getAppParamValue(params, Constant.PWD_MIN_NUM_CHAR)) ? "-1" : Util.getAppParamValue(
                        params, Constant.PWD_MIN_NUM_CHAR)); // 最少数字个数
        model.addAttribute("PWD_MIN_SPECIAL_CHAR", Util.isEmpty(Util.getAppParamValue(params,
                Constant.PWD_MIN_SPECIAL_CHAR)) ? "-1" : Util.getAppParamValue(params, Constant.PWD_MIN_SPECIAL_CHAR)); // 最少特殊字符个数
        model.addAttribute(
                "PWD_CONTAIN_UPPER_CHAR",
                Util.isEmpty(Util.getAppParamValue(params, Constant.PWD_CONTAIN_UPPER_CHAR)) ? "false" : Util
                        .getAppParamValue(params, Constant.PWD_CONTAIN_UPPER_CHAR)); // 必须包括大小写
        return "index";
    }

    /**
     * 跳转到错误页面
     * 
     * @return
     */
    @RequestMapping("/error.do")
    public String error() {
        return "error/error";
    }

}
