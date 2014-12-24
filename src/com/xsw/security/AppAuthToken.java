package com.xsw.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class AppAuthToken extends UsernamePasswordToken {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private int appId;

    public AppAuthToken() {
        super();
    }

    public AppAuthToken(int appId, String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
        this.appId = appId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

}
