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

    // 系统菜单Session名称
    public static final String USER_ROOT_MENU_CTX = "CURRENT_USER_ROOT_MENUCTX";

    // 默认系统Session超时时间
    public static final long DEFAULT_SESSION_TIMEOUT = 30;

    // 系统Session名称
    public static final String PARAM_SESSION_TIMEOUT = "SESSIONTIMEOUT";

    // 是大用户现线数
    public static final String MAX_ONLINE_USER = "MAXONLINEUSER";

    // 异或码长度
    public static final int SALT_SIZE = 8;

    // 密码超过有效期
    public static final Integer USER_PWD_HAS_EXPIRED = 2;

    // 用户失效天数
    public static final String PWD_VALID_DAYNUM = "PWD_VALID_DAYNUM";

    // 用户失效提前提醒天数
    public static final String PWD_FAIL_ADVANCE_DAYS = "PWD_FAIL_ADVANCE_DAYS";

    // 默认密码登录失败次数
    public static final int DEFAULT_PWD_FAIL_ADVANCE_DAYS = 5;

    // 账户超过登录最小到期天数
    public static final Integer USER_ACCOUNT_EXPIRE = 3;

    // 密码最小长度
    public static final String PWD_MIN_LENGTH = "PWD_MIN_LENGTH";

    // 最少字母个数
    public static final String PWD_MIN_ALPHA_CHAR = "PWD_MIN_ALPHA_CHAR";

    // 最少数字个数
    public static final String PWD_MIN_NUM_CHAR = "PWD_MIN_NUM_CHAR";

    // 最少特殊字符个数
    public static final String PWD_MIN_SPECIAL_CHAR = "PWD_MIN_SPECIAL_CHAR";

    // 必须包括大小写
    public static final String PWD_CONTAIN_UPPER_CHAR = "PWD_CONTAIN_UPPER_CHAR";

    // 账户最小到期天数
    public static final String MIN_DAY_ACCOUNT_EXPIRE = "MIN_DAY_ACCOUNT_EXPIRE";

    // 允许初始登录最小天数
    public static final String MIN_DAY_INITIALIZE_LOGIN = "MIN_DAY_INITIALIZE_LOGIN";

    // 用户历史密码次数键值
    public static final String USER_PWD_HIST_SIZE = "PWD_HIST_SIZE";

    // 系统默认分页条数
    public final static int DEFAULT_PAGE_SIZE = 10;

    // UUID 生成名称
    public final static String UUID_TOKEN = "_UUID_TOKEN";

    // random validate code in session key
    public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";

    // Search Key
    public static final String SEARCH_KEY = "_SEARCH_KEY_PARAM";

    // 错误日志记录request键值
    public static final String HANDLE_EXCEPTION = "HANDLE_EXCEPTION";

    // 输入错误密码次数超过最大允许次数
    public static final Integer USER_PWD_LOCK = 1;

    // HASH编码类型
    public static final String HASH_ALGORITHM = "SHA-1";

    // HASH编码长度
    public static final int HASH_INTERATIONS = 1024;

}
