package com.xsw.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2013-09-02
 * @description 系统在线用户统计表
 *
 */

@Entity
@Table(name = "T_AT_SESSION")
public class AtSession extends AbstractEntity implements Serializable {

    /**
     * SESSION ID
     */
    private static final long serialVersionUID = 1L;

    private int userId;
    private String lastOpTime;
    private String sessionId;

    @Id
    @Column(name = "USERID")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "LAST_OP_TIME")
    public String getLastOpTime() {
        return lastOpTime;
    }

    public void setLastOpTime(String lastOpTime) {
        this.lastOpTime = lastOpTime;
    }

    @Column(name = "SESSIONID")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
