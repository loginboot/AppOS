package com.xsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.xsw.model.RoleMenu;

public interface RoleMenuDao extends CrudRepository<RoleMenu, Integer> {

    // 根据角色ID返回关联菜单信息
    @Query("from RoleMenu where role.rid=?1")
    List<RoleMenu> findByRid(int rid);
    
    // 根据角色ID删除角色与权限的关联信息
    @Modifying
    @Query("delete from RoleMenu where role.rid=?1")
    void deleteByRid(int rid);
    
}
