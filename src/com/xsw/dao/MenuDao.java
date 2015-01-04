package com.xsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xsw.model.Menu;

public interface MenuDao extends PagingAndSortingRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {
    
    // 根据用户ID返回对应权限
    @Query("select m from Menu m,RoleMenu rm,UserRole ur where m.mid=rm.menu.mid and rm.role.rid=ur.role.rid "
            +"and ur.user.userId=?1 order by m.depth,m.name,m.mid")
    List<Menu> findMenuByUserId(int userid);
    
    
    // 根据权限标识返回对应的菜单信息
    List<Menu> findByPermission(String permission);

}
