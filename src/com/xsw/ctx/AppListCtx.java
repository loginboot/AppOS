package com.xsw.ctx;

import java.io.Serializable;
import java.util.List;

import com.xsw.model.AppList;
import com.xsw.model.Params;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw  
 * @version 1.0.0
 * @date 2014-12-28
 * @description 系统应用类 - 封装
 *
 */

public class AppListCtx implements Serializable {

    /**
     * APP 1L
     */
    private static final long serialVersionUID = 1L;

    private AppList appList;
    private List<Params> params;

    public AppList getAppList() {
        return appList;
    }

    public void setAppList(AppList appList) {
        this.appList = appList;
    }

    public List<Params> getParams() {
        return params;
    }

    public void setParams(List<Params> params) {
        this.params = params;
    }

}
