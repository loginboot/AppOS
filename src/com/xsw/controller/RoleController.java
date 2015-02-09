package com.xsw.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xsw.ctx.MenuCtx;
import com.xsw.exception.AppException;
import com.xsw.model.Menu;
import com.xsw.model.Role;
import com.xsw.model.RoleMenu;
import com.xsw.model.User;
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

    /**
     * ajax检测角色信息是否已经存在
     * @param name
     * @param rid
     * @return
     */
    @RequestMapping(value = "/role/checkRoleName.do")
    @ResponseBody
    @RequiresPermissions("system-role")
    public Object checkRole(@RequestParam("name") String name, @RequestParam(value = "rid", defaultValue = "0") int rid) {
        User user = Util.getCurrentUser();
        boolean isExist = roleService.isExistRole(user.getAppList().getAppId(), rid, name);
        log.debug("check role for rid:[" + rid + "],name:[" + name + "],exist:[" + isExist + "]...");
        if (isExist) {
            return "false";
        }
        return "true";
    }

    /**
     * 跳转到角色新增页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/role/add.do", method = RequestMethod.GET)
    @RequiresPermissions("system-role-add")
    public String getAddForm(Model model) {
        Date curDate = new Date();
        User user = Util.getCurrentUser();
        Role role = new Role();
        role.setAppList(user.getAppList());
        role.setLastModifyDate(Util.timeStampToStr(curDate));
        // 菜单处理
        parseMenuCtx(user.getAppList().getType(), user.getAppList().getAppId(), model);
        model.addAttribute("role", role);
        model.addAttribute("action", ACTION_ADD);
        return "system/roleForm";
    }

    /**
     * 角色新增
     * @param request
     * @param response
     * @param role
     * @param result
     * @return
     */
    @RequestMapping(value = "/role/add.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-role-add")
    public Object addForm(HttpServletRequest request, HttpServletResponse response, @Valid Role role,
            BindingResult result) {
        // 检测角色是否已经存在
        boolean isExist = roleService.isExistRole(role.getAppList().getAppId(), role.getRid(), role.getName());
        if (isExist) {
            result.addError(new ObjectError("GLOBAL", "ERRCODE.1013"));
        }

        if (result.hasErrors()) {
            return Util.writeJsonValidErrorMsg(messageSource, request, response, result);
        }

        String[] mids = request.getParameterValues("mid");
        User user = Util.getCurrentUser();
        role.setCreateDate(Util.timeStampToStr());
        role.setCreatorUser(user);
        role.setLastModifyDate(Util.timeStampToStr());
        role.setModifierUser(user);
        //save
        roleService.saveOrUpdate(role, mids);

        return Util.writeJsonSuccMsg(messageSource, request, response, MSG_SUCCESS);
    }

    /**
     * 跳转到角色修改页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/role/upd/{id}.do", method = RequestMethod.GET)
    @RequiresPermissions("system-role-upd")
    public String getUpdForm(Model model, @PathVariable("id") int rid) {
        Role role = roleService.findOne(rid);
        // 菜单处理
        parseMenuCtx(role.getAppList().getType(), role.getAppList().getAppId(), model);
        
        // 已选菜单
        List<RoleMenu> rmlst = roleService.findRoleMenuByRid(rid);
        Map<Integer,String> checked = new HashMap<Integer,String>();
        for(RoleMenu rm : rmlst){
            checked.put(rm.getMenu().getMid(), "checked='checked'");
        }
        model.addAttribute("checked", checked);
        model.addAttribute("role", role);
        model.addAttribute("action", ACTION_UPD);
        return "system/roleForm";
    }

    /**
     * 角色信息修改
     * @param request
     * @param response
     * @param role
     * @param result
     * @return
     */
    @RequestMapping(value = "/role/upd.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-role-upd")
    public Object updForm(HttpServletRequest request, HttpServletResponse response, @Valid Role role,
            BindingResult result) {
        // 角色最后更新比较
        Role old = roleService.findOne(role.getRid());
        if (old == null) {
            result.addError(new ObjectError("GLOBAL", "ERRCODE.1013"));
        } else if (role.getLastModifyDate().compareTo(old.getLastModifyDate()) != 0) {
            result.addError(new ObjectError("GLOBAL", "ERRCODE.1013"));
        }

        // 检测角色是否已经存在
        boolean isExist = roleService.isExistRole(role.getAppList().getAppId(), role.getRid(), role.getName());
        if (isExist) {
            result.addError(new ObjectError("GLOBAL", "ERRCODE.1013"));
        }

        if (result.hasErrors()) {
            return Util.writeJsonValidErrorMsg(messageSource, request, response, result);
        }

        String[] mids = request.getParameterValues("mid");
        User user = Util.getCurrentUser();
        role.setCreateDate(old.getCreateDate());
        role.setCreatorUser(old.getCreatorUser());
        role.setLastModifyDate(Util.timeStampToStr());
        role.setModifierUser(user);
        //save
        roleService.saveOrUpdate(role, mids);

        return Util.writeJsonSuccMsg(messageSource, request, response, MSG_SUCCESS);
    }

    /**
     * 删除角色信息
     * @param request
     * @param response
     * @param rid
     * @return
     * @throws AppException
     */
    @RequestMapping(value = "/role/del.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-role-del")
    public Object delForm(HttpServletRequest request, HttpServletResponse response, @RequestParam("rid") int rid)
            throws AppException {
        try {
            roleService.delete(rid);
        } catch (Exception e) {
            throw new AppException("ERRCODE.2201");
        }
        return Util.writeJsonSuccMsg(messageSource, request, response, MSG_SUCCESS);
    }

    /**
     * 跳转到角色查看页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/role/view/{id}.do", method = RequestMethod.GET)
    @RequiresPermissions("system-role")
    public String getViewForm(Model model, @PathVariable("id") int rid) {
        Role role = roleService.findOne(rid);
        
        // 菜单处理
        parseMenuCtx(role.getAppList().getType(), role.getAppList().getAppId(), model);
        
        // 已选菜单
        List<RoleMenu> rmlst = roleService.findRoleMenuByRid(rid);
        Map<Integer,String> checked = new HashMap<Integer,String>();
        for(RoleMenu rm : rmlst){
            checked.put(rm.getMenu().getMid(), "checked='checked'");
        }
        model.addAttribute("checked", checked);
        model.addAttribute("role", role);
        model.addAttribute("action", ACTION_VIEW);
        model.addAttribute("disabled", ACTION_DISABLED);
        return "system/roleForm";
    }

    private void parseMenuCtx(int type, int appId, Model model) {
        List<Menu> mlist = roleService.getMenuBySysOrNor(type, appId);
        // 解析菜单层次
        List<MenuCtx> menuCtx = Util.convertMenusToMenuCtxs(mlist);
        model.addAttribute("mctx", menuCtx);
    }

}
