package com.xsw.controller;

import javax.annotation.Resource;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

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

public class BaseController {
    @Resource
    protected ReloadableResourceBundleMessageSource messageSource;

    @Resource
    protected AppCtx appctx;

    protected JsonMapper mapper = new JsonMapper();
}
