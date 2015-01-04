package com.xsw.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xsw.model.AppList;
import com.xsw.service.AppListService;
import com.xsw.utils.Servlets;
import com.xsw.utils.Util;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2015-01-04
 * @description 系统应用控制类
 *
 */

@Controller
@RequestMapping("/system")
@RequiresAuthentication
public class AppListController extends BaseController {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(AppListController.class);

    @Resource
    private AppListService appListService;

    /**
     * 跳转到应用查看列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/appList.do", method = RequestMethod.GET)
    @RequiresPermissions("system-appList")
    public String list(HttpServletRequest request) {
        log.debug("get app for url:[appList.do]...");
        Util.initPage(request);
        return "system/appList";
    }

    /**
     * 返回用户JSON分页数据
     * @param request
     * @param response
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/appList.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-appList")
    public Object list(HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize) {
        // 排序
        Sort sort = null;
        // 查询条件
        Map<String, Object> search = Servlets.getParametersStartingWith(request, "search_");
        // 执行
        Page<AppList> ls = appListService.findByPage(page, pageSize, search, sort);
        // 保存查询条件
        Util.storeSearchKeyValue(request, search, "search_");
        // 返回JSON数据
        return Util.writePagableJson(ls);
    }

    /**
     * 跳转到应用修改页面
     * @param model
     * @param appId
     * @return
     */
    @RequestMapping(value = "/appList/upd/{id}.do", method = RequestMethod.GET)
    @RequiresPermissions("system-appList-upd")
    public String getUpdForm(Model model, @PathVariable("id") int appId) {
        AppList app = appListService.findOne(appId);
        model.addAttribute("appList", app);
        model.addAttribute("action", ACTION_UPD);
        return "system/appList";
    }

}
