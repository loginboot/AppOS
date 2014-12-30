package com.xsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.xsw.model.Menu;

public interface MenuDao extends CrudRepository<Menu, Integer> {
    
    // 根据用户ID返回对应权限
    @Query("select m from Menu m,RoleMenu rm,UserRole ur where m.mid=rm.menu.mid and rm.role.rid=ur.user.rid "
            +"and ur.user.userId=? order by order by m.depth,m.name,m.mid")
    List<Menu> findMenuByUserId(int userid);

}
