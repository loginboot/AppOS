package com.xsw.ctx;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.xsw.model.Menu;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-12-28
 * @description 系统菜单 封装类
 *
 */

public class MenuCtx implements Serializable {

    /**
     * 1L
     */
    private static final long serialVersionUID = 1L;

    private Menu menu;
    private int depth = 0;// 菜单层级
    private MenuCtx parent;
    private List<MenuCtx> children = new LinkedList<MenuCtx>();

    public MenuCtx(Menu menu) {
        setMenu(menu);
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
        this.depth = menu.getDepth();
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * 父节点
     * @return
     */
    public MenuCtx getParent() {
        return parent;
    }

    public void setParent(MenuCtx parent) {
        this.parent = parent;
    }

    /**
     * 是否根节点
     * 
     * @return true.是 false.否
     */
    public boolean isRoot() {
        return parent == null ? true : false;
    }

    /**
     * 是否存在子Menu
     * 
     * @return true.是 false.否
     */
    public boolean hasChildren() {
        return children.size() > 0 ? true : false;
    }

    /**
     * 增加子MenuCtx
     * 
     * @param child 子MenuCtx
     */
    public void addChildMenuCtx(MenuCtx child) {
        children.add(child);
    }

    public List<MenuCtx> getChildren() {
        return children;
    }

    public void setChildren(List<MenuCtx> children) {
        this.children = children;
    }

}
