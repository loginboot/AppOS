package com.xsw.service;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xsw.constant.Constant;
import com.xsw.constant.Status;
import com.xsw.constant.Type;
import com.xsw.ctx.MenuCtx;
import com.xsw.dao.AppListDao;
import com.xsw.dao.AtSessionDao;
import com.xsw.dao.MenuDao;
import com.xsw.dao.ParamsDao;
import com.xsw.dao.RoleDao;
import com.xsw.dao.UserDao;
import com.xsw.model.AppList;
import com.xsw.model.AtSession;
import com.xsw.model.Menu;
import com.xsw.model.Params;
import com.xsw.model.Role;
import com.xsw.model.User;
import com.xsw.utils.Encrypt;
import com.xsw.utils.Util;

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

    @Resource
    private MenuDao menuDao;

    @Resource
    private ParamsDao paramsDao;

    @Resource
    private AtSessionDao atSessionDao;

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
     * 返回所有正常的应用列表數據
     * @return
     */
    public List<AppList> findAllApp(){
        return appListDao.findByStatus(Status.NORMAL);
    }
    
    /**
     * 根据应用ID返回登录的应用数据
     * @param appId
     * @return
     */
    public AppList findByAppId(int appId){
        return appListDao.findOne(appId);
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

    /**
     * 根据用户ID获取菜单信息
     * 
     * @param uId
     * @return
     */
    public List<Menu> findMenuByUserId(int userId) {
        return menuDao.findMenuByUserId(userId);
    }

    /**
     * 获取指定应用ID对应的参数信息
     * 
     * @param cId
     * @return List<Params>
     */
    public List<Params> getAppParams(int cid) {
        return paramsDao.findParamsByAppId(cid);
    }

    /**
     * 根据用户ID获取对应用记的菜单信息
     * 
     * @param uId
     * @return List<MenuCtx>
     */
    public List<MenuCtx> getRootMenuCtx(int userId) {
        List<Menu> menus = findMenuByUserId(userId);
        // 将Menu解析为层次结构
        return Util.convertMenusToMenuCtxs(menus);
    }

    /**
     * 更新用戶登錄失敗次數
     * 
     * @param uId
     * @throws LyodsException
     */
    public void updateFailureTimes(int AppId, String loginName) {
        User user = findByLoginName(AppId, loginName);
        int pwdFailMaxTimes = Constant.DEFAULT_PWD_MAXINUM_TIMES;
        List<Params> params = getAppParams(AppId);
        String pvals = Util.getAppParamValue(params, Constant.PARAM_PASSWORD_MAXFAILTIMES);
        if (!Util.isEmpty(pvals)) {
            pwdFailMaxTimes = Integer.parseInt(pvals);
        }
        user.setFailTimes(user.getFailTimes() + 1);
        if ((user.getFailTimes()) == pwdFailMaxTimes) {
            user.setStatus(Status.LOCK);
            user.setLockReason(Constant.USER_PWD_LOCK);
        }
        userDao.save(user);
    }

    /**
     * 用户登录成功
     * 
     * @param loginName
     */
    public void loginSuccessful(int AppId, String loginName, String sessionId) {
        User user = findByLoginName(AppId, loginName);
        if (user.getFailTimes() > 0) {
            user.setFailTimes(0);
        }
        // 更新用户最后登录日期
        user.setLastLoginDate(Util.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        userDao.save(user);
        // insert into AtSession
        AtSession as = new AtSession();
        as.setUserId(user.getUserId());
        as.setLastOpTime(Util.timeStampToStr(new Date()));
        as.setSessionId(sessionId);
        atSessionDao.save(as);
    }

    /**
     * 用户退出
     * 
     * @param uid
     * @param sessionId
     */
    public void logoutSuccessful(int userId, String sessionId) {
        atSessionDao.deleteByUidAndSessionId(userId, sessionId);
    }

    /**
     * 检查session在数据库中的状态，如果合法则更新最近操作时间
     * 
     * @param uid 用户id
     * @param cid 客户id
     * @param sessionid
     * @return 0.正常 1.sessionid不一致 2.超时 3.数据库没有记录
     */
    public int check(final User user, final AppList appList, String sessionid) {
        AtSession as = atSessionDao.findOne(user.getUserId());
        if (as == null) {
            return 3;
        }
        // 判断是否已经被重新登录过
        if (!sessionid.trim().equals(as.getSessionId().trim())) {
            return 1;
        }
        // 判断是否超时
        List<Params> pls = getAppParams(appList.getAppId());
        long timeout = Constant.DEFAULT_SESSION_TIMEOUT;
        String timeStr = Util.getAppParamValue(pls, Constant.PARAM_SESSION_TIMEOUT);
        if (!Util.isEmpty(timeStr)) {
            timeout = Long.parseLong(timeStr);
        }
        log.debug("SESSION TIMEOUT VALUE[" + timeout + "] MINUTE");
        try {
            Date lst_op_time = Util.strToTimeStamp(as.getLastOpTime());
            Date curtime = new Date();
            long retaintime = (curtime.getTime() - lst_op_time.getTime()) / 1000 / 60; // 得到分钟
            // 超时
            if (retaintime > timeout) {
                return 2;
            }
        } catch (Exception e) {
            log.error("[" + as + "]parse lst_op_time fail,", e);
        }
        // 正常则更新操作时间
        as.setLastOpTime(Util.timeStampToStr(new Date()));
        atSessionDao.save(as);
        return 0;
    }

    /**
     * @param cid 客户id 删除所有超时的Session记录
     */
    public void deleteTimeOutSession(int appId) {
        List<Params> pls = getAppParams(appId);
        long timeout = Constant.DEFAULT_SESSION_TIMEOUT;
        String timeStr = Util.getAppParamValue(pls, Constant.PARAM_SESSION_TIMEOUT);
        if (!Util.isEmpty(timeStr)) {
            timeout = Long.parseLong(timeStr);
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, (int) timeout * -1);
        log.debug("Delete all timeout session which lastOpTime <= " + Util.timeStampToStr(cal.getTime()));
        atSessionDao.deleteTimeoutSession(Util.timeStampToStr(cal.getTime()));
    }

    /***
     * 
     * 检查当前在线用户数据 是否已经 超上限
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public boolean checkOnlineUser(String expStr, int cid, long userId) {
        deleteTimeOutSession(cid);
        String[] coutStr = null;
        try {
            coutStr = Encrypt.decodeString(expStr).split(":");
        } catch (UnsupportedEncodingException e) {
            log.warn("decode user max size error:",e);
            return true;
        }
        Integer max = Integer.parseInt(coutStr[1]);
        List<AtSession> aslst = (List<AtSession>) atSessionDao.findAll();
        for (AtSession as : aslst) {
            if (as.getUserId() == userId) {
                return true;
            }
        }
        if (max != 0 && max <= aslst.size()) {
            return false;
        }
        return true;
    }

    /**
     * 用户license过期
     * 
     * @param expired
     * @return
     */
    public boolean checkExpiredDate(String expired) {
        boolean result = true;
        try {
            String[] coutStr = Encrypt.decodeString(expired).split(":");
            if (coutStr.length >= 4) {
                int expiredDay = Integer.parseInt(coutStr[3]);
                int days = Integer.parseInt(Util.dateToStr(new Date(), "yyyyMMdd"));
                if (days > expiredDay) {
                    result = false;
                }
            }
        } catch (Exception e) {
            log.warn("decode license expired error:",e);
            result = false;
        }
        return result;
    }

}
