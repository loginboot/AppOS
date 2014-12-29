package com.xsw.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xsw.constant.Type;
import com.xsw.dao.AppListDao;
import com.xsw.dao.RoleDao;
import com.xsw.dao.UserDao;
import com.xsw.model.AppList;
import com.xsw.model.Role;
import com.xsw.model.User;

@Service
public class LoginService {
    /**
     * 日志
     */
    private static Logger log = LoggerFactory.getLogger(LoginService.class);

    @Resource
    private AppListDao appListDao;

    @Resource
    private UserDao userDao;

    @Resource
    private RoleDao roleDao;

    /**
     * 返回主系统应用信息
     * @return
     */
    public AppList findSystemApp() {
        AppList app = appListDao.findOne(Type.SYSTEM_APP);
        log.debug("loading main app info:[" + app + "]...");
        return app;
    }

    /**
     * 登陆用户信息返回
     * @param appId
     * @param loginName
     * @return
     */
    public User findByLoginName(int appId, String loginName) {
        return userDao.findByAppIdAndLoginName(appId, loginName);
    }

    /**
     * 根据用户ID返回角色信息
     * @param userId
     * @return
     */
    public List<Role> findRoleByUserId(int userId) {
        return roleDao.findRoleByUserId(userId);
    }
}
