package com.xsw.controller;

import java.util.Calendar;
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

import com.xsw.aspect.AppLog;
import com.xsw.constant.Constant;
import com.xsw.constant.ExpStatusEnum;
import com.xsw.constant.Status;
import com.xsw.constant.Type;
import com.xsw.exception.AppException;
import com.xsw.model.Params;
import com.xsw.model.Role;
import com.xsw.model.User;
import com.xsw.model.UserRole;
import com.xsw.service.LoginService;
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

    @Resource
    private LoginService loginService;

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
        user.setInitPwd(1);
        Util.entryptPassword(user, user.getPassword());
        User currUser = Util.getCurrentUser();
        user.setCreatorUser(currUser);
        user.setCreateDate(Util.timeStampToStr());
        user.setModifierUser(currUser);
        user.setLastModifyDate(Util.timeStampToStr());

        // 最后登录日期
        setUserLastLoginDate(user, currUser.getAppList().getAppId());

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
    public String getUpdForm(HttpServletRequest request, @PathVariable("id") int id, Model model) {
        User user = userService.findOne(id);
        List<Role> roles = userService.findAppRole(user.getAppList().getAppId());
        UserRole userRole = userService.findUserRoleById(id);

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("userRole", userRole);
        model.addAttribute("action", ACTION_UPD);
        model.addAttribute("statusMap", ExpStatusEnum.statusMap);
        // 用户状态锁定原因
        if (user.getStatus() == Status.LOCK) {
            String code = ExpStatusEnum.lockReasonMap.get(user.getLockReason());
            if (!Util.isEmpty(code)) {
                model.addAttribute("lockReason", Util.getMessageByCode(messageSource, request, code));
            }
        }
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
    public Object updForm(HttpServletRequest request, HttpServletResponse response, @RequestParam("rid") int rid,
            @Valid User user, BindingResult result) {
        User curUser = Util.getCurrentUser();
        // check user has same login name for same client
        if (userService.isExistUser(curUser.getAppList().getAppId(), user.getLoginName(), user.getUserId())) {
            result.addError(new ObjectError("GLOBAL", "ERRCODE.1013"));
        }
        User old = userService.findOne(user.getUserId());
        if (old == null) {
            result.addError(new ObjectError("GLOBAL", "ERRCODE.1013"));
        } else if (old.getLastModifyDate().compareTo(user.getLastModifyDate()) != 0) {
            result.addError(new ObjectError("lastModifyDate", "ERRCODE.1013"));
        }

        if (result.hasErrors()) {
            return Util.writeJsonValidErrorMsg(messageSource, request, response, result);
        }

        Date curDate = new Date();
        user.setInitPwd(old.getInitPwd());
        user.setFailTimes(old.getFailTimes());
        user.setPassword(old.getPassword());
        user.setSalt(old.getSalt());
        user.setCreateDate(old.getCreateDate());
        user.setCreatorUser(old.getCreatorUser());
        user.setLockReason(old.getLockReason());
        user.setPwdLastModifyDate(old.getPwdLastModifyDate());
        user.setHistoryPwd(old.getHistoryPwd());
        user.setLastLoginDate(old.getLastLoginDate());

        // 用户状态信息处理
        if (user.getStatus() == Status.NORMAL) {
            user.setLockReason(null);
            user.setFailTimes(Status.NORMAL);
            if (Status.LOCK == old.getStatus()) {
                setUserLastLoginDate(user, curUser.getAppList().getAppId());
            }
        }

        user.setLastModifyDate(Util.timeStampToStr(curDate));
        user.setModifierUser(curUser);
        // update user and userRole
        userService.saveOrUpdate(user, rid);
        return Util.writeJsonSuccMsg(messageSource, request, response, MSG_SUCCESS);
    }

    /**
     * 删除用户
     * 
     * @param request
     * @param response
     * @param model
     * @param redirectAttributes
     * @param id
     * @return
     * @throws AppException 
     * @throws LyodsException
     */
    @RequestMapping(value = "/user/del.do", method = RequestMethod.POST)
    @ResponseBody
    @AppLog(position = 2, details = "Delete Record ID:")
    @RequiresPermissions("system-user-del")
    public Object delAjax(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") int id)
            throws AppException {
        try {
            userService.delete(id);
        } catch (Exception e) {
            throw new AppException("ERRCODE.2201");
        }
        return Util.writeJsonSuccMsg(messageSource, request, response, "MSGCODE.0000");
    }

    /**
     * 跳转到用户查看页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/user/view/{id}.do", method = RequestMethod.GET)
    @RequiresPermissions("system-user")
    public String getViewForm(HttpServletRequest request, @PathVariable("id") int id, Model model) {
        User user = userService.findOne(id);
        List<Role> roles = userService.findAppRole(user.getAppList().getAppId());
        UserRole userRole = userService.findUserRoleById(id);

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("userRole", userRole);
        model.addAttribute("action", ACTION_VIEW);
        model.addAttribute("statusMap", ExpStatusEnum.statusMap);
        // 用户状态锁定原因
        if (user.getStatus() == Status.LOCK) {
            String code = ExpStatusEnum.lockReasonMap.get(user.getLockReason());
            if (!Util.isEmpty(code)) {
                model.addAttribute("lockReason", Util.getMessageByCode(messageSource, request, code));
            }
        }
        model.addAttribute("disabled", ACTION_DISABLED);
        return "system/userForm";
    }

    /**
     * 重置用户密码
     * 
     * @param request
     * @param response
     * @param userid
     * @return
     */
    @RequestMapping(value = "/user/resetPwd.do")
    @ResponseBody
    @AppLog(position = 2, details = "Reset Password ID:")
    @RequiresPermissions("system-user-rpw")
    public Object resetPwd(HttpServletRequest request, HttpServletResponse response, @RequestParam("userId") int userId) {
        User old = userService.findOne(userId);
        String strNewPwd = Util.randomBase62(8);
        old.setInitPwd(1);
        old.setFailTimes(0);
        setUserLastLoginDate(old, old.getAppList().getAppId());

        userService.resetPassword(old, strNewPwd);
        return Util.writeJsonSuccMsg(messageSource, request, response, "MSGCODE.0000", "\"resetpwd\":\"" + strNewPwd
                + "\"");
    }

    /**
     * 跳转到用户设置界面
     * 
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/setting.do")
    @RequiresAuthentication
    public String getSetting(HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = userService.findOne(Util.getCurrentUser().getUserId());
        UserRole userRole = userService.findUserRoleById(user.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("roleName", userRole.getRole().getName());

        // 密码安全等控制
        List<Params> params = loginService.getAppParams(user.getAppList().getAppId());
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
        return "system/setting";
    }

    /**
     * 更新用户设置信息
     * 
     * @param request
     * @param response
     * @param user
     * @param result
     * @return
     * @throws LyodsException
     */
    @RequestMapping(value = "/user/updUserInfo.do")
    @ResponseBody
    @AppLog(position = 2, name = "loginName")
    @RequiresAuthentication
    public Object updateUserInfo(HttpServletRequest request, HttpServletResponse response, @Valid User user,
            BindingResult result) {
        User old = userService.findOne(user.getUserId());
        old.setName(user.getName());
        old.setEmail(user.getEmail());
        old.setAddress(user.getAddress());
        old.setFailTimes(0);
        old.setFax(user.getFax());
        // old.setInitPwd(user.getInitPwd());
        old.setMobile(user.getMobile());
        old.setPhone(user.getPhone());
        old.setModifierUser(Util.getCurrentUser());
        old.setLastModifyDate(Util.timeStampToStr(new Date()));
        userService.update(old, Type.USER_UPD_SETTING);
        return Util.writeJsonSuccMsg(messageSource, request, response, "MSGCODE.0000");
    }

    /**
     * 更新用户安全设置信息
     * 
     * @param request
     * @param response
     * @param uId
     * @param oldpassword
     * @param password
     * @return
     * @throws LyodsException
     */
    @RequestMapping(value = "/user/updSecurity.do")
    @ResponseBody
    @AppLog(position = 2, details = "Security update for User ID:")
    @RequiresAuthentication
    public Object updateSecurity(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("userId") int userId, @RequestParam("oldpassword") String oldpassword,
            @RequestParam("password") String password) throws AppException {
        User old = userService.findOne(userId);
        if (!old.getPassword().equals(Util.entryptPassword(oldpassword, old.getSalt()))) {
            throw new AppException("ERRCODE.1003");
        } else if (!Util.isEmpty(old.getHistoryPwd())) {
            String[] saltPwds = Util.trim(old.getHistoryPwd()).split("\\x01");
            String[] sps = null;
            for (String sp : saltPwds) {
                sps = sp.split("\\x02");
                if (Util.entryptPassword(password, sps[0]).equals(sps[1])) {
                    throw new AppException("ERRCODE.pwdHistoryExist");
                }
            }
        }
        old.setInitPwd(0);
        old.setFailTimes(0);
        Util.entryptPassword(old, password);
        old.setModifierUser(Util.getCurrentUser());
        old.setLastModifyDate(Util.timeStampToStr(new Date()));
        handleUserHistPwd(old); // 处理用户历史密码
        old.setPwdLastModifyDate(Util.timeStampToStr(new Date()));

        userService.update(old, Type.USER_UPD_SETTING);
        return Util.writeJsonSuccMsg(messageSource, request, response, "MSGCODE.0000");
    }

    /**
     * 密码初始化
     * 
     * @param request
     * @param response
     * @param userid
     * @param oldpassword
     * @param password
     * @return
     * @throws AppException 
     * @throws LyodsException
     */
    @RequestMapping(value = "/initPwd.do")
    @ResponseBody
    @AppLog(position = 2, details = "Init Password For User ID:")
    @RequiresAuthentication
    public Object updInitPwd(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("userId") int userId, @RequestParam("oldpassword") String oldpassword,
            @RequestParam("password") String password) throws AppException {
        User old = userService.findOne(userId);
        if (!old.getPassword().equals(Util.entryptPassword(oldpassword, old.getSalt()))) {
            throw new AppException("ERRCODE.1003");
        } else if (!Util.isEmpty(old.getHistoryPwd())) {
            String[] saltPwds = Util.trim(old.getHistoryPwd()).split("\\x01");
            String[] sps = null;
            for (String sp : saltPwds) {
                sps = sp.split("\\x02");
                if (Util.entryptPassword(password, sps[0]).equals(sps[1])) {
                    throw new AppException("ERRCODE.pwdHistoryExist");
                }
            }
        }
        old.setInitPwd(0);
        old.setFailTimes(0);
        old.setPassword(Util.entryptPassword(password, old.getSalt()));
        handleUserHistPwd(old); // 处理用户历史密码
        old.setPwdLastModifyDate(Util.timeStampToStr(new Date()));

        userService.update(old, Type.USER_UPD_INITPWD);
        User user = Util.getCurrentUser();
        user.setInitPwd(0); // 更session数据
        return Util.writeJsonSuccMsg(messageSource, request, response, "MSGCODE.0000");
    }

    /*
     * 封装用户历史密码记录信息
     */
    private void handleUserHistPwd(User user) {
        // 更新历史密码
        List<Params> pls = loginService.getAppParams(user.getAppList().getAppId());
        String hisSize = Util.getAppParamValue(pls, Constant.USER_PWD_HIST_SIZE);
        if (!Util.isEmpty(hisSize)) {
            int size = Integer.parseInt(hisSize);
            String[] histpwd = Util.trim(user.getHistoryPwd()).split("\\x01");
            StringBuffer buf = new StringBuffer();
            int start = 0;
            if (histpwd.length >= size) {
                start = histpwd.length - (size - 1);
            }
            boolean first = true;
            for (int i = start; i < histpwd.length; i++) {
                if (first) {
                    buf.append((char) 0x1);
                    first = false;
                }
                buf.append(histpwd);
            }
            if (!Util.isEmpty(buf.toString())) {
                buf.append((char) 0x1);
            }
            buf.append(user.getSalt() + ((char) 0x2) + user.getPassword());
        } else {
            if (Util.isEmpty(user.getHistoryPwd())) {
                user.setHistoryPwd(user.getSalt() + ((char) 0x2) + user.getPassword());
            } else {
                user.setHistoryPwd(user.getHistoryPwd() + ((char) 0x1) + user.getSalt() + ((char) 0x2)
                        + user.getPassword());
            }
        }
    }

    // 处理用户最后登录日期
    private void setUserLastLoginDate(User user, int appId) {
        String lastLoginDate = null;
        List<Params> params = loginService.getAppParams(appId);
        String minDayAccountExpire = Util.getAppParamValue(params, Constant.MIN_DAY_ACCOUNT_EXPIRE);
        String minDayInitializeLogin = Util.getAppParamValue(params, Constant.MIN_DAY_INITIALIZE_LOGIN);
        // 如果“账户最小到期天数，允许初始登录最小天数”任意一个为空时，都把最后登录时间设置为空
        if (!Util.isEmpty(minDayAccountExpire) && !Util.isEmpty(minDayInitializeLogin)) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -Integer.parseInt(minDayAccountExpire));
            cal.add(Calendar.DATE, +Integer.parseInt(minDayInitializeLogin));
            lastLoginDate = Util.timeStampToStr(cal.getTime());
        }
        user.setLastLoginDate(lastLoginDate);
    }

}
