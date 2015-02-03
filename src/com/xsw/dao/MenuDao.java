package com.xsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xsw.constant.Type;
import com.xsw.model.Menu;

public interface MenuDao extends PagingAndSortingRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {

    // 根据用户ID返回对应权限
    @Query("select m from Menu m,RoleMenu rm,UserRole ur where m.mid=rm.menu.mid and rm.role.rid=ur.role.rid "
            + "and ur.user.userId=?1 order by m.mid")
    List<Menu> findMenuByUserId(int userid);

    // 返回系统菜单
    @Query("select m from Menu m where m.range in(" + Type.MENU_RANGE_SYSINT + "," + Type.MENU_RANGE_BOTH
            + ") order by mid")
    List<Menu> findSystemMenu();

    // 返回客户菜单
    @Query("select m from Menu m where m.range in(" + Type.MENU_RANGE_CLIENT + "," + Type.MENU_RANGE_BOTH
            + ") order by mid")
    List<Menu> findAppMenu();

    // 不是系统客户，一般客户添加角色时所查询的菜单
    @Query("select m from Menu m,AppMenu am where m.mid=am.menu.mid and am.appList.appId=?1 order by m.mid")
    List<Menu> findAppMenuAppId(int appId);

    // 根据权限标识返回对应的菜单信息
    List<Menu> findByPermission(String permission);

}
