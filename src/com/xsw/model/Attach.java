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

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-09-08
 * @description 系统附件信息表
 *
 */

@Entity
@Table(name = "T_ATTACH")
public class Attach extends AbstractEntity implements Serializable {

    /**
     * ATTACH ID
     */
    private static final long serialVersionUID = 1L;
    private int attachId;
    private int seq;
    private int sourceId;
    private String sourceTable;
    private String attachName;
    private String attachDesc;
    private byte[] attachData;

    @Id
    @SequenceGenerator(name = "SEQ_T_ATTACH", sequenceName = "SEQ_T_ATTACH", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_T_ATTACH")
    @Column(name = "ATTACH_ID", nullable = false)
    public int getAttachId() {
        return attachId;
    }

    public void setAttachId(int attachId) {
        this.attachId = attachId;
    }

    @Column(name = "SEQ", nullable = false)
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Column(name = "SOURCE_ID", nullable = false)
    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    @Column(name = "SOURCE_TABLE", nullable = false)
    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    @Column(name = "ATTACH_NAME")
    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    @Column(name = "ATTACH_DESC")
    public String getAttachDesc() {
        return attachDesc;
    }

    public void setAttachDesc(String attachDesc) {
        this.attachDesc = attachDesc;
    }

    @Column(name = "ATTACH_DATA")
    public byte[] getAttachData() {
        return attachData;
    }

    public void setAttachData(byte[] attachData) {
        this.attachData = attachData;
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
