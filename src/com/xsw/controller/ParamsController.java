package com.xsw.controller;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xsw.model.AppList;
import com.xsw.model.Params;
import com.xsw.service.ParamsService;
import com.xsw.utils.Servlets;
import com.xsw.utils.Util;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2015-02-17
 * @description 通用系统参数设置
 *
 */

@Controller
@RequestMapping("/system")
@RequiresAuthentication
public class ParamsController extends BaseController {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(ParamsController.class);

    @Resource
    private ParamsService paramsService;

    /**
     * 
     * @param request
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/params.do", method = RequestMethod.GET)
    @RequiresPermissions("system-params")
    public String getParams(HttpServletRequest request, Model model) {

        int appId = Util.getCurrentUser().getAppList().getAppId();

        List<String> appParams = (List<String>) appctx.getObjParam("appParams");
        List<Params> params = paramsService.findParamsByAppId(appId);
        List<Params> outparams = new Vector<Params>();
        for (String strParam : appParams) {
            String[] p = strParam.split(";");
            Params par = new Params();
            par.setName(p[0]); //key
            par.setNotes(p[1]); //label 
            for (Params tmp : params) {
                //set value
                if (par.getName().equals(tmp.getName())) {
                    par.setValue(tmp.getValue());
                    par.setPid(tmp.getPid());
                }
            }
            if (p.length >= 3) {
                par.setPattern(p[2]);
            }
            log.debug("App Params:" + par);
            outparams.add(par);
        }
        model.addAttribute("appParams", outparams);

        List<String> pwdControlParamValues = (List<String>) appctx.getObjParam("pwdControl");
        List<Params> pwdControlParams = new Vector<Params>();
        for (String strParam : pwdControlParamValues) {
            String[] p = strParam.split(";");
            Params par = new Params();
            par.setName(p[0]); //key
            par.setNotes(p[1]); //label 
            for (Params tmp : params) {
                //set value
                if (par.getName().equals(tmp.getName())) {
                    par.setValue(tmp.getValue());
                    par.setPid(tmp.getPid());
                }
            }
            if (p.length >= 3) {
                par.setPattern(p[2]);
            }
            log.debug("pwdControl Params:" + par);
            pwdControlParams.add(par);
        }
        model.addAttribute("pwdControlParams", pwdControlParams);

        // 是否有修改权限
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isPermitted("system-params-upd")) {
            model.addAttribute("disabled", "disabled='disabled'");
        }

        model.addAttribute("action", ACTION_UPD);
        return "system/paramsForm";
    }

    /**
     * 
     * @param request
     * @param response
     * @param ps
     * @param result
     * @return
     */
    @RequestMapping(value = "/params/upd.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-params-upd")
    public Object updParams(HttpServletRequest request, HttpServletResponse response, @Valid Params ps,
            BindingResult result) {

        // 数据检验
        validParams(result, request, "appParams");
        if (result.hasErrors()) {
            return Util.writeJsonValidErrorMsg(messageSource, request, response, result);
        }

        Map<String, Object> urlParams = Servlets.getParametersStartingWith(request, "pid_");
        List<Params> params = new Vector<Params>();
        AppList app = Util.getCurrentUser().getAppList();
        for (String keys : urlParams.keySet()) {
            Params par = new Params();
            par.setName(keys); // key
            par.setValue(request.getParameter(keys)); // label
            par.setAppList(app);
            int pid = Integer.parseInt((String) urlParams.get(keys));
            if (pid > 0) {
                par.setPid(pid);
            }
            params.add(par);
        }
        log.debug("updateAppParams-->" + params);
        paramsService.saveParams(params);

        return Util.writeJsonSuccMsg(messageSource, request, response, MSG_SUCCESS);
    }

    /**
     * 
     * @param request
     * @param response
     * @param ps
     * @param result
     * @return
     */
    @RequestMapping(value = "/params/pwdControl/upd.do", method = RequestMethod.POST)
    @RequiresPermissions("system-params-upd")
    @ResponseBody
    public Object updPwdControlParam(HttpServletRequest request, HttpServletResponse response, @Valid Params ps,
            BindingResult result) {
        // 数据检验
        validParams(result, request, "pwdControl");
        if (result.hasErrors()) {
            return Util.writeJsonValidErrorMsg(messageSource, request, response, result);
        }
        int appId = Util.getCurrentUser().getAppList().getAppId();
        AppList app = Util.getCurrentUser().getAppList();
        List<Params> originParams = paramsService.findParamsByAppId(appId);
        Map<String, Object> urlParams = Servlets.getParametersStartingWith(request, "pid_");
        Params par = null;
        for (String keys : urlParams.keySet()) {
            int pid = Integer.parseInt((String) urlParams.get(keys));
            if (pid > 0) {
                for (Params p : originParams) {
                    if (p.getPid() == pid) {
                        par = p;
                        par.setValue(request.getParameter(keys)); // label
                        par.setAppList(app);
                        break;
                    }
                }
            } else {
                par = new Params();
                par.setName(keys); // key
                par.setValue(request.getParameter(keys)); // label
                par.setAppList(app);
                originParams.add(par);
            }
        }
        log.debug("--->save pwdConrol params:" + originParams);
        paramsService.saveParams(originParams);

        return Util.writeJsonSuccMsg(messageSource, request, response, "MSGCODE.0000");
    }

    /**
     * 检验公用参数
     * @param result
     * @param vals
     * @param type
     */
    @SuppressWarnings("unchecked")
    private void validParams(BindingResult result, HttpServletRequest request, String type) {
        List<String> keys = (List<String>) appctx.getObjParam(type);
        for (String str : keys) {
            String[] p = str.split(";");
            String key = p[0];
            String val = request.getParameter(key);
            // 需要校验
            if (p.length >= 2) {
                String[] pats = Util.trim(p[2]).split(" ");
                for (String pat : pats) {
                    // 是否必填
                    if ("required".equals(pat) && Util.isEmpty(val)) {
                        result.addError(new ObjectError("GLOBAL", Util.getMessageByCode(messageSource, request, p[1])
                                + "&nbsp;:&nbsp;" + Util.getMessageByCode(messageSource, request, "PUB.required")));
                        break;
                    }
                    // 是否数字
                    if (Util.trim(pat).startsWith("digits") && !Util.isEmpty(val)) {
                        try {
                            Integer intval = Integer.parseInt(val);
                            if (intval < 0) {
                                result.addError(new ObjectError("GLOBAL", Util.getMessageByCode(messageSource, request,
                                        p[1])
                                        + "&nbsp;:&nbsp;"
                                        + Util.getMessageByCode(messageSource, request, "PUB.digits")));
                                break;
                            }
                        } catch (Exception e) {
                            result.addError(new ObjectError("GLOBAL", Util.getMessageByCode(messageSource, request,
                                    p[1])
                                    + "&nbsp;:&nbsp;"
                                    + Util.getMessageByCode(messageSource, request, "PUB.digits")));
                            break;
                        }
                    }
                    // 最大长度
                    if (Util.trim(pat).startsWith("maxlength") && !Util.isEmpty(val)) {
                        String[] lens = Util.trim(pat).split("=");
                        int len = 0;
                        try {
                            len = Integer.parseInt(lens[1]);
                        } catch (Exception e) {
                            log.error("ParseInt maxlength error:", e);
                        }
                        if (Util.trim(val).getBytes().length > len) {
                            result.addError(new ObjectError("GLOBAL", Util.getMessageByCode(messageSource, request,
                                    p[1])
                                    + "&nbsp;:&nbsp;"
                                    + Util.getMessageByCode(messageSource, request, "PUB.maxlength") + len));
                            break;
                        }
                    }
                    // 邮箱验证
                    if (Util.trim(pat).startsWith("email") && !Util.isEmpty(val)) {
                        Pattern regx = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
                        Matcher mat = regx.matcher(val);
                        if (!mat.matches()) {
                            result.addError(new ObjectError("GLOBAL", Util.getMessageByCode(messageSource, request,
                                    p[1])
                                    + "&nbsp;:&nbsp;"
                                    + Util.getMessageByCode(messageSource, request, "PUB.emailError")));
                            break;
                        }
                    }
                    // true or false
                    if (Util.trim(pat).startsWith("trueOrFalse") && !Util.isEmpty(val)) {
                        if (!"true".equals(val) && !"false".equals(val)) {
                            result.addError(new ObjectError("GLOBAL", Util.getMessageByCode(messageSource, request,
                                    p[1])
                                    + "&nbsp;:&nbsp;"
                                    + Util.getMessageByCode(messageSource, request, "PUB.trueOrFalse")));
                            break;
                        }
                    }// end if
                } // end for
            } // end if 
        } // end for
    }

}
