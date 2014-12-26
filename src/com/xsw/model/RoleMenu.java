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

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2013-10-10
 * @description 角色权限表
 * 
 */
@Entity
@Table(name = "T_ROLE_MENU")
public class RoleMenu extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long rmId;
    private Role role;
    private Menu menu;

    @Id
    @SequenceGenerator(name = "SEQ_T_ROLE_MENU", sequenceName = "SEQ_T_ROLE_MENU", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_T_ROLE_MENU")
    @Column(name = "RMID", nullable = false)
    public long getRmId() {
        return rmId;
    }

    public void setRmId(long rmId) {
        this.rmId = rmId;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "RID")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "MID")
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
