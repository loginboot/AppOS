package com.xsw.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xsw.ctx.MenuCtx;
import com.xsw.model.AppList;
import com.xsw.model.AppMenu;
import com.xsw.model.Menu;
import com.xsw.service.MenuService;
import com.xsw.utils.Util;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2015-01-10
 * @description 应用菜单数据维护 - 创建
 *
 */

@Controller
@RequestMapping("/system")
@RequiresAuthentication
public class MenuController extends BaseController {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(MenuController.class);

    @Resource
    private MenuService menuService;

    /**
     * 跳转到菜单维护页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/menu.do", method = RequestMethod.GET)
    @RequiresPermissions("system-menu")
    public String list(HttpServletRequest request, Model model) {
        log.debug("setting system menu for:[menu.do]...");
        List<AppList> applst = menuService.findAllApp();
        // 菜单查询
        Map<String, List<MenuCtx>> mctxMap = parseMenuCtx(request);
        model.addAttribute("applst", applst);
        model.addAttribute("mctxMap", mctxMap);
        model.addAttribute("action", ACTION_UPD);
        return "system/menu";
    }

    /**
     * 返回一个菜单使用的菜单信息
     * @param request
     * @param response
     * @param appId
     * @return
     */
    @RequestMapping(value = "/menu/getMenuForApp.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-menu")
    public Object getMenuForApp(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("appId") int appId) {
        // 已选择的菜单
        List<AppMenu> amlst = menuService.findByAppId(appId);
        Map<Integer, String> checked = new HashMap<Integer, String>();
        for (AppMenu am : amlst) {
            checked.put(am.getMenu().getMid(), "checked='checked'");
        }
        String resc = "\"checked\":" + mapper.toJson(checked);
        log.debug("menu checked result:" + resc);
        return Util.writeJsonSuccMsg(messageSource, request, response, "MSGCODE.0000", resc);
    }

    /**
     * 修改菜单信息
     * @param request
     * @param response
     * @param appId
     * @return
     */
    @RequestMapping(value = "/menu/upd.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-menu-upd")
    public Object updFuncMenu(HttpServletRequest request, HttpServletResponse response, @RequestParam("appId") int appId) {
        String[] mids = request.getParameterValues("mid");

        //save 
        menuService.saveOrUpdate(mids, appId);

        return Util.writeJsonSuccMsg(messageSource, request, response, "MSGCODE.0000");
    }

    /*
     * 菜单树生成
     */
    @SuppressWarnings("unchecked")
    private Map<String, List<MenuCtx>> parseMenuCtx(HttpServletRequest request) {
        List<Menu> mlist = menuService.findBySystemMenu();
        List<String> applst = (List<String>) appctx.getObjParam("systemOS");
        Map<String, List<MenuCtx>> mctxMap = new LinkedHashMap<String, List<MenuCtx>>();
        for (String app : applst) {
            String[] valArr = Util.trim(app).split("\\|");
            int min = Integer.parseInt(valArr[0]);
            int max = Integer.parseInt(valArr[1]);
            // 
            String appKey = Util.getMessageByCode(messageSource, request, valArr[2]);
            List<Menu> tmplst = new ArrayList<Menu>();
            for (Menu m : mlist) {
                if (m.getMid() >= min && m.getMid() <= max) {
                    tmplst.add(m);
                }
            }

            // 解析菜单层次
            List<MenuCtx> menuCtx = Util.convertMenusToMenuCtxs(tmplst);
            mctxMap.put(appKey, menuCtx);
        }
        return mctxMap;
    }
}
