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

import com.xsw.model.User;
import com.xsw.service.UserService;
import com.xsw.utils.Servlets;
import com.xsw.utils.Util;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-12-27
 * @description 系统用户控制类 - 统一用户管理
 *
 */

@Controller
@RequestMapping("/system")
@RequiresAuthentication
public class UserController extends BaseController {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(UserController.class);

    @Resource
    private UserService userService;

    /**
     * 跳转用户列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/user.do", method = RequestMethod.GET)
    @RequiresPermissions("system-user")
    public String list(HttpServletRequest request) {
        log.debug("get user list for url:[user.do]...");
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
    @RequestMapping(value = "/user.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-user")
    public Object list(HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize) {
        // 排序
        Sort sort = null;
        // 查询条件
        Map<String, Object> search = Servlets.getParametersStartingWith(request, "search_");
        // 执行
        Page<User> ls = userService.findByPage(page, pageSize, search, sort);
        // 返回JSON数据
        return Util.writePagableJson(ls);
    }
}
