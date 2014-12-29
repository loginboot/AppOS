package com.xsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xsw.model.Role;

public interface RoleDao extends PagingAndSortingRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    // 根据用户ID返回角色信息
    @Query("select r from Role r,UserRole ur where r.rid=ur.rid and ur.userId=?1")
    List<Role> findRoleByUserId(int userId);
    
}
