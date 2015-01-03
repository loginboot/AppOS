package com.xsw.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2013-10-09
 * @description 系统日志表
 * 
 */
@Entity
@Table(name = "T_AT_LOG")
public class AtLog extends AbstractEntity implements Serializable {
    /**
     * LID
     */
    private static final long serialVersionUID = 1L;

    private int lid;
    private AppList appList;
    private User user;
    private String operTime;
    private int type;
    private String remoteHost;
    private String permission;
    private String details;

    @Id
    @SequenceGenerator(name = "SEQ_T_AT_LOG", sequenceName = "SEQ_T_AT_LOG", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_T_AT_LOG")
    @Column(name = "LID", nullable = false)
    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    @ManyToOne
    @JoinColumn(name = "APP_ID")
    @NotFound(action = NotFoundAction.IGNORE)
    public AppList getAppList() {
        return appList;
    }

    public void setAppList(AppList appList) {
        this.appList = appList;
    }

    @ManyToOne
    @JoinColumn(name = "USERID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NotBlank
    @Length(max = 26)
    @Column(name = "OPER_TIME")
    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    @NotNull
    @Column(name = "TYPE")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @NotBlank
    @Length(max = 64)
    @Column(name = "REMOTE_HOST")
    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    @Column(name = "PERMISSION")
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @NotBlank
    @Column(name = "DETAILS")
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
