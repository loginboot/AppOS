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

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creatro xiesw
 * @version 1.0.0
 * @date 2013-08-09
 * @description 系统角色表
 * 
 */

@Entity
@Table(name = "T_ROLE")
public class Role extends AbstractEntity implements Serializable {
    /**
     * RID
     */
    private static final long serialVersionUID = 1L;
    private int rid;
    private AppList appList;
    private String name;
    private String description;

    @Id
    @SequenceGenerator(name = "SEQ_T_ROLE", sequenceName = "SEQ_T_ROLE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_T_ROLE")
    @Column(name = "RID", nullable = false)
    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

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

    @Length(max = 200)
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
