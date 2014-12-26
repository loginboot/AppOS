package com.xsw.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2013-09-01
 * @description 系统权限表
 * 
 */

@Entity
@Table(name = "T_MENU")
public class Menu extends AbstractEntity implements Serializable {
    /**
     * MID
     */
    private static final long serialVersionUID = 1L;
    private int mid;
    private String name;
    private int type;
    private int pid;
    private String uri;
    private String permission;
    private int depth;
    private int range;

    @Id
    @Column(name = "MID", nullable = false)
    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
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

    @NotNull
    @Column(name = "TYPE")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @NotNull
    @Column(name = "pid")
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Length(max = 100)
    @Column(name = "URI")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @NotBlank
    @Length(max = 100)
    @Column(name = "PERMISSION")
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @NotNull
    @Column(name = "DEPTH")
    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @NotNull
    @Column(name = "RANGE")
    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
