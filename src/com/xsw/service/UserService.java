package com.xsw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.xsw.dao.RoleDao;
import com.xsw.dao.UserDao;
import com.xsw.dao.UserRoleDao;
import com.xsw.model.Role;
import com.xsw.model.User;
import com.xsw.model.UserRole;
import com.xsw.utils.Util;

@Service
public class UserService extends BaseService {
    /**
     * 日志
     */
    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserDao userDao;

    @Resource
    private UserRoleDao userRoleDao;
    
    @Resource
    private RoleDao roleDao;

    /**
     * 根据查询条件进行系统用户列表
     * 
     * @param page
     * @param pageSize
     * @param search
     * @param sort
     * @return
     */
    @SuppressWarnings("unchecked")
    public Page<User> findByPage(int page, int pageSize, Map<String, Object> search, Sort sort) {
        log.debug("search system user by page:["+page+"],pageSize:["+pageSize+"].");
        if (sort == null) {
            sort = new Sort(Direction.ASC, "userId");
        }
        PageRequest pageRequest = new PageRequest(page - 1, pageSize, sort);
        return userDao.findAll((Specification<User>) buildSpecification(search, User.class), pageRequest);
    }

    /**
     * 根据用户ID返回一条用户记录
     * 
     * @param userId
     * @return
     */
    public User findOne(int userId) {
        return userDao.findOne(userId);
    }
    
    /**
     * 检测系统用户登录名称是否已经存在
     * 
     * @param cid
     * @param loginName
     * @param userId
     * @return
     */
    public boolean isExistUser(int appId, String loginName, int userId) {
        int count = userDao.findByUserForExists(appId, userId, loginName);
        if (count > 0) {
            return true;
        }
        return false;
    }
    
    /**
     * 根据应用ID获取所有ROLE
     * 
     * @param cid
     * @return
     */
    public List<Role> findAppRole(int appId) {
        return roleDao.findByAppId(appId);
    }
    
    /**
     * 返回一条用户与角色关联信息
     * @param id
     * @return
     */
    public UserRole findUserRoleById(int id){
        List<UserRole> urlst = userRoleDao.findByUser_userId(id);
        if(urlst.size()!=0){
            return urlst.get(0);
        }
        return null;
    }
    


    /**
     * 保存用户信息同时保存用户与角色关联信息
     * @param user
     * @param rid
     */
    public void saveOrUpdate(User user, int rid) {
        log.debug("save system user:[" + user + "],and rid:[" + rid + "].");
        User tmp = userDao.save(user);
        UserRole ur = new UserRole();
        // 判断用户是修改还是新增
        if (user.getUserId() == 0) {
            List<UserRole> urls = userRoleDao.findByUser_userId(user.getUserId());
            if (urls.size() == 1) {
                ur = urls.get(0);
            }
        }
        Role role = new Role();
        role.setRid(rid);
        ur.setRole(role);
        ur.setUser(tmp);
    }
    
    /**
     * 单独更新用户信息
     * 
     * @param user
     */
    public void update(User user, int type) {
        log.debug("...update user for name is:" + user.getName());
        userDao.save(user);
    }
    
    /**
     * 用户重置密码
     * 
     * @param user
     * @param plainPwd
     */
    public void resetPassword(User user, String plainPwd) {
        Util.entryptPassword(user, plainPwd);
        userDao.save(user);
        // send email to notify
        if (!Util.isEmpty(user.getEmail())) {
            log.debug("...reset password for user email is:[" + user.getEmail() + "]...");
            HashMap<String, Object> model = new HashMap<String, Object>();
            model.put("userName", user.getName());
            model.put("loginName", user.getLoginName());
            model.put("newpassword", plainPwd);
          //  emailSender.sendEmail(model, "resetpwd", user.getEmail());
        }
    }

    /**
     * 删除用户信息
     * @param userId
     */
    public void delete(int userId) {
        log.debug("delete user by userId:[" + userId + "].");
        userRoleDao.delete(userId);
        userDao.delete(userId);
    }
}
