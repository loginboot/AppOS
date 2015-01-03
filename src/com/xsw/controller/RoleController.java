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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xsw.model.Role;
import com.xsw.service.RoleService;
import com.xsw.utils.Servlets;
import com.xsw.utils.Util;

@Controller
@RequestMapping("/system")
@RequiresAuthentication
public class RoleController extends BaseController {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(RoleController.class);

    @Resource
    private RoleService roleService;

    /**
     * 跳转用户列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/role.do", method = RequestMethod.GET)
    @RequiresPermissions("system-role")
    public String list(HttpServletRequest request) {
        log.debug("get role list for url:[role.do]...");
        // 初始化分页信息
        Util.initPage(request);
        return "system/role";
    }

    /**
     * 返回用户JSON分页数据
     * @param request
     * @param response
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/role.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-role")
    public Object list(HttpServletRequest request, HttpServletResponse response, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize) {
        // 排序
        Sort sort = null;
        // 查询条件
        Map<String, Object> search = Servlets.getParametersStartingWith(request, "search_");
        // 执行
        Page<Role> ls = roleService.findByPage(page, pageSize, search, sort);
        // 返回JSON数据
        return Util.writePagableJson(ls);
    }

    @RequestMapping(value = "/role/add.do", method = RequestMethod.GET)
    @RequiresPermissions("system-role-add")
    public String getAddForm(Model model) {
        Role role = new Role();
        model.addAttribute("role", role);
        model.addAttribute("action", ACTION_ADD);
        return "system/roleForm";
    }
    
    
    

}
