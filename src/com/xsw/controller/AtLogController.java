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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xsw.model.AtLog;
import com.xsw.service.AtLogService;
import com.xsw.utils.Servlets;
import com.xsw.utils.Util;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2015-01-02
 * @description 系统日志 - 统一日志管理
 *
 */

@Controller
@RequestMapping("/system")
@RequiresAuthentication
public class AtLogController extends BaseController{

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(AtLogController.class);
    
    @Resource
    private AtLogService atLogService;
    
    /**
     * 跳转用户列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/atlog.do", method = RequestMethod.GET)
    @RequiresPermissions("system-atlog")
    public String list(HttpServletRequest request) {
        log.debug("get atlog list for url:[atlog.do]...");
        // 初始化分页信息
        Util.initPage(request);
        return "system/user";
    }

    /**
     * 返回用户JSON分页数据
     * @param request
     * @param response
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/atlog.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-atlog")
    public Object list(HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize) {
        // 排序
        Sort sort = null;
        // 查询条件
        Map<String, Object> search = Servlets.getParametersStartingWith(request, "search_");
        // 执行
        Page<AtLog> ls = atLogService.findByPage(page, pageSize, search, sort);
        // 返回JSON数据
        return Util.writePagableJson(ls);
    }
    
}
