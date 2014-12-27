package com.xsw.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class AppListController extends BaseController{


    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(AppListController.class);
    
    
    public String list(HttpServletRequest request)
    {
        
        return "system/appList";
    }
}
