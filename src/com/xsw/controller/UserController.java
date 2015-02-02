package com.xsw.controller;

import java.util.Date;
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

import com.xsw.constant.ExpStatusEnum;
import com.xsw.model.Role;
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
    
    /**
     * 检查用户登陆名称是否已经存在
     * 
     * @param loginName
     * @param uId
     * @return
     */
    @RequestMapping(value = "/user/checkUserName.do")
    @ResponseBody
    @RequiresAuthentication
    public String checkUserName(@RequestParam("loginName") String loginName,
            @RequestParam(value = "userId", defaultValue = "0") int userId) {
        User user = Util.getCurrentUser();
        log.debug("....checkUserName(" + loginName + "," + userId + ")-");
        if (userService.isExistUser(user.getAppList().getAppId(), Util.trim(loginName), userId)) { // 存在則報錯
            return "false";
        } else {
            return "true";
        }
    }

    /**
     * 跳转到用户新增页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/user/add.do", method = RequestMethod.GET)
    @RequiresPermissions("system-user-add")
    public String getAddForm(Model model) {
        Date curDate = new Date();
        User newUser = new User();
        User currUser = Util.getCurrentUser();
        newUser.setLastModifyDate(Util.timeStampToStr(curDate));
        newUser.setModifierUser(currUser);
        newUser.setAppList(currUser.getAppList());

        List<Role> roles = userService.findAppRole(currUser.getAppList().getAppId());
        model.addAttribute("user", newUser);
        model.addAttribute("roles", roles);
        model.addAttribute("statusMap", ExpStatusEnum.statusMap);
        model.addAttribute("action", ACTION_ADD);
        return "system/userForm";
    }

    /**
     * 系统用户信息新增
     * @param request
     * @param response
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/user/add.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-user-add")
    public Object addForm(HttpServletRequest request, HttpServletResponse response, @RequestParam("rid") int rid,
            @Valid User user, BindingResult result) {

        User curUser = Util.getCurrentUser();
        // check user has same login name for same client
        if (userService.isExistUser(curUser.getAppList().getAppId(), user.getLoginName(), user.getUserId())) {
            result.addError(new ObjectError("GLOBAL", "ERRCODE.1013"));
        }

        if (result.hasErrors()) {
            return Util.writeJsonValidErrorMsg(messageSource, request, response, result);
        }

        User currUser = Util.getCurrentUser();
        user.setCreatorUser(currUser);
        user.setCreateDate(Util.timeStampToStr());
        user.setModifierUser(currUser);
        user.setLastModifyDate(Util.timeStampToStr());

        //save 
        userService.saveOrUpdate(user, rid);

        return Util.writeJsonSuccMsg(messageSource, request, response, MSG_SUCCESS);
    }

    /**
     * 跳转到用户修改页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/user/upd/{id}.do", method = RequestMethod.GET)
    @RequiresPermissions("system-user-upd")
    public String getUpdForm(@PathVariable("id") int id, Model model) {
        User user = userService.findOne(id);
        model.addAttribute("user", user);
        model.addAttribute("action", ACTION_UPD);
        return "system/userForm";
    }

    /**
     * 用户修改
     * @param request
     * @param response
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/user/upd.do", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("system-user-upd")
    public Object updForm(HttpServletRequest request, HttpServletResponse response, @Valid User user,
            BindingResult result) {
        // 检测用户信息是否一致
        User old = userService.findOne(user.getUserId());
        if (old == null) {

        } else if (user.getLastModifyDate().compareTo(old.getLastModifyDate()) != 0) {

        }

        if (result.hasErrors()) {

        }

        return Util.writeJsonSuccMsg(messageSource, request, response, MSG_SUCCESS);
    }

    /**
     * 跳转到用户查看页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/user/view/{id}.do", method = RequestMethod.GET)
    @RequiresPermissions("system-user")
    public String getViewForm(@PathVariable("id") int id, Model model) {
        User user = userService.findOne(id);
        model.addAttribute("user", user);
        model.addAttribute("action", ACTION_VIEW);
        model.addAttribute("disabled", ACTION_DISABLED);
        return "system/userForm";
    }

}
