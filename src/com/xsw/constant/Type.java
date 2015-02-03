package com.xsw.constant;

public class Type {
    
    // 总系统类型 编号
    public final static int SYSTEM_APP = 1; 

    /**
     * 客户分类-系统内
     */
    public final static int APP_SYSINT = 0;
    /**
     * 客户分类-普通客户
     */
    public final static int APP_NORMAL = 1;
    
    /**
     * 菜单分类-功能项
     */
    public final static int MENU_FUNCTION = 1;

    /**
     * 菜单分类-菜單&功能
     */
    public final static int MENU_BOTH = 2;

    /**
     * 菜单范围-系统
     */
    public final static int MENU_RANGE_SYSINT = 0;
    
    /**
     * 菜单范围-客户
     */
    public final static int MENU_RANGE_CLIENT = 1;

    /**
     * 菜单范围-系统&客户
     */
    public final static int MENU_RANGE_BOTH = 2;
    
    /**
     * 用户更新类型
     */
    public final static int USER_UPD_INITPWD = 0;// 用户初始密码
    public final static int USER_UPD_RESETPWD = 1;// 用户重置密码
    public final static int USER_UPD_SETTING = 2;// 用户设置更新
}
