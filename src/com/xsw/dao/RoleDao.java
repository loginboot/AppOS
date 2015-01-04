package com.xsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xsw.model.Role;

public interface RoleDao extends PagingAndSortingRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    // 根据用户ID返回角色信息
    @Query("select r from Role r,UserRole ur where r.rid=ur.role.rid and ur.user.userId=?1")
    List<Role> findRoleByUserId(int userId);

    // 判断相同应用是否有相同角色名称
    @Query("select count(*) from Role where appList.appId=?1 and rid!=?2 and name=?3")
    int findByAppIdAndName(int appId, int rid, String name);

}
