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
 * Table T_CLIENT_MENU
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2013-09-01
 * @description 系统应用菜单关联表
 * 
 */
@Entity
@Table(name = "T_APP_MENU")
public class AppMenu extends AbstractEntity implements Serializable {

    /**
     * CMID
     */
    private static final long serialVersionUID = 1L;
    private int amId;
    private AppList appList;
    private Menu menu;

    @Id
    @SequenceGenerator(name = "SEQ_T_CLIENT_MENU", sequenceName = "SEQ_T_CLIENT_MENU", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_T_CLIENT_MENU")
    @Column(name = "CMID", nullable = false)
    public int getAmId() {
        return amId;
    }

    public void setAmId(int amId) {
        this.amId = amId;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "APP_ID")
    public AppList getAppList() {
        return appList;
    }

    public void setAppList(AppList appList) {
        this.appList = appList;
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
