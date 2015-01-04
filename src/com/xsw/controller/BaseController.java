package com.xsw.controller;

import javax.annotation.Resource;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;

import com.xsw.ctx.AppCtx;
import com.xsw.mapper.JsonMapper;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-12-27
 * @description 公用控制层类
 *
 */
@Controller
public class BaseController {
    @Resource
    protected ReloadableResourceBundleMessageSource messageSource;

    @Resource
    protected AppCtx appctx;

    protected JsonMapper mapper = new JsonMapper();
    // 成功返回信息标识码
    protected static String MSG_SUCCESS = "MSGCODE.0000";
    // 系统操作 - 新增
    protected static String ACTION_ADD = "add";
    // 系统操作 - 修改
    protected static String ACTION_UPD = "upd";
    // 系统操作 - 查看
    protected static String ACTION_VIEW = "view";
    // 系统操作 - 锁定
    protected static String ACTION_DISABLED = "disabled='disabled'";
}
