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

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2013-09-09
 * @description 应用选列表
 * 
 */
@Entity
@Table(name = "T_APP_LIST")
public class AppList extends AbstractEntity implements Serializable {
    /**
     * CID
     */
    private static final long serialVersionUID = 1L;
    private int appId;
    private String chiName;
    private String engName;
    private String displayName;
    private String urlName;
    private String remark;
    private int status;

    private String version;

    @Id
    @SequenceGenerator(name = "SEQ_T_APP_LIST", sequenceName = "SEQ_T_APP_LIST", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_T_APP_LIST")
    @Column(name = "APP_ID", nullable = false)
    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    @Length(max = 200)
    @Column(name = "CHI_NAME")
    public String getChiName() {
        return chiName;
    }

    public void setChiName(String chiName) {
        this.chiName = chiName;
    }

    @Length(max = 200)
    @Column(name = "ENG_NAME")
    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    @Length(max = 200)
    @Column(name = "DISPLAY_NAME")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @NotBlank
    @Length(max = 20)
    @Column(name = "URL_NAME")
    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @NotNull
    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "VERSION")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
