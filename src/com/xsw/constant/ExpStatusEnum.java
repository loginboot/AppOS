package com.xsw.constant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExpStatusEnum {
    // 操作类型 1.新增 2.修改3.删除4.其它
    public static final Map<Integer, String> actionTypes = new LinkedHashMap<Integer, String>();
    static {
        actionTypes.put(1, "PUB.action1");
        actionTypes.put(2, "PUB.action2");
        actionTypes.put(3, "PUB.action3");
        actionTypes.put(4, "PUB.action4");
    }

    // 用户登录失败原因
    public static final HashMap<Integer, String> lockReasonMap = new HashMap<Integer, String>();
    static {
        lockReasonMap.put(Constant.USER_PWD_LOCK, "org.apache.shiro.authc.ExcessiveAttemptsException");
        lockReasonMap.put(Constant.USER_PWD_HAS_EXPIRED, "ERRCODE.userPwdHasExpired");
        lockReasonMap.put(Constant.USER_ACCOUNT_EXPIRE, "ERRCODE.userAccountExpired");
    }

    // 系统状态
    public static final Map<Integer, String> statusMap = new HashMap<Integer, String>();
    static {
        statusMap.put(0, "PUB.status_0");
        statusMap.put(1, "PUB.status_1");
        statusMap.put(2, "PUB.status_2");
    }

    // 客户类型
    public static final Map<Integer, String> clientType = new HashMap<Integer, String>();
    static {
        clientType.put(0, "PUB.range_0");
        clientType.put(1, "PUB.range_1");
    }
}
