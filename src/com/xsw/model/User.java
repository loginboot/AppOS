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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2013-08-09
 * @description 系统用户表
 * 
 */
@Entity
@Table(name = "T_USER")
public class User extends AbstractEntity implements Serializable {
    /**
     * USERID
     */
    private static final long serialVersionUID = 1L;
    private int userId;
    private AppList appList;
    private String name;
    private String loginName;
    private String email;
    private String phone;
    private String mobile;
    private String fax;
    private String address;
    private String salt;
    private String password;
    private int failTimes;
    private int initPwd;
    private int status;
    private String historyPwd;
    private Integer lockReason;
    private String pwdLastModifyDate;
    private String lastLoginDate;

    private int hasDays;

    @Id
    @SequenceGenerator(name = "SEQ_T_USER", sequenceName = "SEQ_T_USER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_T_USER")
    @Column(name = "USERID", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "APP_ID")
    public AppList getAppList() {
        return appList;
    }

    public void setAppList(AppList appList) {
        this.appList = appList;
    }

    @NotBlank
    @Length(max = 100)
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    @Length(max = 50)
    @Column(name = "LOGIN_NAME")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Email
    @Length(max = 100)
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(max = 50)
    @Column(name = "PHONE")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(max = 30)
    @Column(name = "MOBILE")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Length(max = 50)
    @Column(name = "FAX")
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Length(max = 300)
    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonIgnore
    @Length(max = 128)
    @Column(name = "SALT")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    // @NotBlank
    @Length(max = 200)
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotNull
    @Column(name = "FAIL_TIMES")
    public int getFailTimes() {
        return failTimes;
    }

    public void setFailTimes(int failTimes) {
        this.failTimes = failTimes;
    }

    @NotNull
    @Column(name = "INIT_PWD")
    public int getInitPwd() {
        return initPwd;
    }

    public void setInitPwd(int initPwd) {
        this.initPwd = initPwd;
    }

    @NotNull
    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "HISTORY_PWD")
    public String getHistoryPwd() {
        return historyPwd;
    }

    public void setHistoryPwd(String historyPwd) {
        this.historyPwd = historyPwd;
    }

    @Column(name = "LOCK_REASON")
    public Integer getLockReason() {
        return lockReason;
    }

    public void setLockReason(Integer lockReason) {
        this.lockReason = lockReason;
    }

    @Column(name = "PWD_LAST_MODIFYDATE")
    public String getPwdLastModifyDate() {
        return pwdLastModifyDate;
    }

    public void setPwdLastModifyDate(String pwdLastModifyDate) {
        this.pwdLastModifyDate = pwdLastModifyDate;
    }

    @Transient
    public int getHasDays() {
        return hasDays;
    }

    public void setHasDays(int hasDays) {
        this.hasDays = hasDays;
    }

    @Column(name = "LAST_LOGIN_DATE")
    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    // -------------Common Data-----------------
    private User creatorUser;
    private String createDate;
    private User modifierUser;
    private String lastModifyDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CREATOR_ID")
    public User getCreatorUser() {
        return creatorUser;
    }

    @Transient
    public String getCreatorUserName() {
        if (creatorUser != null) {
            return creatorUser.getName();
        }
        return "";
    }

    public void setCreatorUser(User creatorUser) {
        this.creatorUser = creatorUser;
    }

    @Column(name = "CREATE_DATE")
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "MODIFIER_ID")
    public User getModifierUser() {
        return modifierUser;
    }

    @Transient
    public String getModifierUserName() {
        if (modifierUser != null) {
            return modifierUser.getName();
        }
        return "";
    }

    public void setModifierUser(User modifierUser) {
        this.modifierUser = modifierUser;
    }

    @NotBlank
    @Column(name = "LAST_MODIFY_DATE")
    public String getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(String lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }
}
