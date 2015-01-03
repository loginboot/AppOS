package com.xsw.security;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.xsw.constant.Constant;
import com.xsw.constant.Status;
import com.xsw.model.Menu;
import com.xsw.model.Params;
import com.xsw.model.Role;
import com.xsw.model.User;
import com.xsw.service.LoginService;
import com.xsw.utils.Encodes;
import com.xsw.utils.Util;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw   
 * @version 1.0.0
 * @date 2015-01-03
 * @description 系统用户登录检验模块
 *
 */

public class ShiroDbRealm extends AuthorizingRealm {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(ShiroDbRealm.class);

    @Autowired
    private LoginService loginService;

    /**
     * 用于存储该用户的角色和功能权限 授权查询回调函数, 进行鉴权且缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 设置角色
        List<Role> rlst = loginService.findRoleByUserId(user.getUserId());
        if (rlst != null) {
            for (Role r : rlst) {
                info.addRole(r.getName());
            }
        }

        //设置权限
        List<Menu> mlst = loginService.findMenuByUserId(user.getUserId());
        if (mlst != null) {
            for (Menu m : mlst) {
                info.addStringPermission(m.getPermission());
            }
        }
        return info;
    }

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AppAuthToken appToken = (AppAuthToken) token;
        log.info("doGetAuthenticationInfo--" + appToken);
        User user = null;
        try {
            user = loginService.findByLoginName(appToken.getAppId(), appToken.getUsername());
        } catch (Exception e) {
            log.error("doGetAuthenticationInfo--exception,", e);
        }

        if (user != null) {
            // 用户最大登录次数
            int pwdFailMaxTimes = Constant.DEFAULT_PWD_MAXINUM_TIMES;
            List<Params> params = loginService.getAppParams(appToken.getAppId());
            String pvals = Util.getAppParamValue(params, Constant.PARAM_PASSWORD_MAXFAILTIMES);
            if (!Util.isEmpty(pvals)) {
                pwdFailMaxTimes = Integer.parseInt(pvals);
            }
            if (pwdFailMaxTimes != 0 && user.getFailTimes() > pwdFailMaxTimes) {
                throw new ExcessiveAttemptsException();
            }
            // 用户状态
            if (user.getStatus() != Status.NORMAL) {
                throw new LockedAccountException();
            }

            // 用户上线
            if (!loginService.checkOnlineUser(Util.getAppParamValue(params, Constant.MAX_ONLINE_USER),
                    appToken.getAppId(), user.getUserId())) {
                throw new AuthenticationException("ERRCODE.1008");
            }

            // 用户许可是否过期
            if (!loginService.checkExpiredDate(Util.getAppParamValue(params, Constant.MAX_ONLINE_USER))) {
                throw new ExpiredCredentialsException("ERRCODE.1009");
            }

            // 失效日期
            int pwdValidDayNum = 0;
            if (!Util.isEmpty(Util.getAppParamValue(params, Constant.PWD_VALID_DAYNUM))) {
                pwdValidDayNum = Integer.parseInt(Util.getAppParamValue(params, Constant.PWD_VALID_DAYNUM));
            }

            // 用户密码已经超时未修改
            String pwdLastModifyDate = user.getPwdLastModifyDate();
            if (!Util.isEmpty(pwdLastModifyDate)) {
                // 默认提前多少天提醒用户修改密码
                int pwdFailAdvanceDays = Constant.DEFAULT_PWD_FAIL_ADVANCE_DAYS;
                String pwdFailStr = Util.getAppParamValue(params, Constant.PWD_FAIL_ADVANCE_DAYS);
                if (!Util.isEmpty(pwdFailStr)) {
                    pwdFailAdvanceDays = Integer.parseInt(pwdFailStr);
                }
                long haveNotChangePwdDays = 0;
                try {
                    String dateStr = Util.dateToStr(new Date(), "yyyy-MM-dd");
                    Date end = Util.strToDate(dateStr);
                    Date start = Util.strToDateTime(pwdLastModifyDate, "yyyy-MM-dd");
                    haveNotChangePwdDays = (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);
                } catch (ParseException e) {
                    log.error("parse to exirpe date error!", e);
                }
                if (pwdValidDayNum != 0 && haveNotChangePwdDays > pwdValidDayNum) {
                    user.setInitPwd(1); // 跳到index.jsp的时候，让用户重新修改密码
                    user.setLockReason(Constant.USER_PWD_HAS_EXPIRED);
                } else {
                    user.setLockReason(null);
                }
                if (pwdValidDayNum != 0 && haveNotChangePwdDays >= (pwdValidDayNum - pwdFailAdvanceDays)) {
                    int hasDays = (int) (pwdValidDayNum - haveNotChangePwdDays + 1);
                    user.setHasDays(hasDays);
                }
            }

            // log.info("entrypt password:"+Util.entryptPassword("admin",user.getSalt())+",salt:"+user.getSalt());
            byte[] salt = Encodes.decodeHex(Util.trim(user.getSalt()));
            SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(user, user.getPassword(),
                    ByteSource.Util.bytes(salt), getName());
            return sai;
        } else {
            throw new UnknownAccountException();
        }
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Constant.HASH_ALGORITHM);
        matcher.setHashIterations(Constant.HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }

}
