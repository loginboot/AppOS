package com.xsw.constant;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2-014-12-27
 * @description 系统常量类 - 基本常量
 *
 */

public class Constant {

    // 默认最大登录失败次数
    public static final int DEFAULT_PWD_MAXINUM_TIMES = 5;

    // 密码最大失败参数名称
    public static final String PARAM_PASSWORD_MAXFAILTIMES = "PWDMAXFAILTIMES";
    
    // 系统应用Session名称
    public static final String SESSION_APP_LIST = "SESSION_APP_LIST";

    // 默认系统Session超时时间
    public static final long DEFAULT_SESSION_TIMEOUT = 30;
    
    // 系统Session名称
    public static final String PARAM_SESSION_TIMEOUT = "SESSIONTIMEOUT";

    // 系统默认分页条数
    public final static int DEFAULT_PAGE_SIZE = 10;

    // UUID 生成名称
    public final static String UUID_TOKEN = "_UUID_TOKEN";

    // 错误日志记录request键值
    public static final String HANDLE_EXCEPTION = "HANDLE_EXCEPTION";

    // 输入错误密码次数超过最大允许次数
    public static final Integer USER_PWD_LOCK = 1;

}
