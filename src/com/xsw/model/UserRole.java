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
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2013-10-10
 * @description 用户角色关联表
 * 
 */
@Entity
@Table(name = "T_USER_ROLE")
public class UserRole extends AbstractEntity implements Serializable {
    /**
     * URID
     */
    private static final long serialVersionUID = 1L;
    private int urId;
    private User user;
    private Role role;

    @Id
    @SequenceGenerator(name = "SEQ_T_USER_ROLE", sequenceName = "SEQ_T_USER_ROLE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_T_USER_ROLE")
    @Column(name = "URID", nullable = false)
    public int getUrId() {
        return urId;
    }

    public void setUrId(int urId) {
        this.urId = urId;
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
    @JoinColumn(name = "USERID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
