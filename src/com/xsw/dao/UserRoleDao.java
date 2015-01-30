package com.xsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.xsw.model.UserRole;

public interface UserRoleDao extends CrudRepository<UserRole, Integer> {

    // 根据用户ID返回用户与角色的关联信息
    List<UserRole> findByUser_userId(int userId);

    // 根据用户ID删除用户与角色的关联信息
    @Modifying
    @Query("delete from UserRole where user.userId=?1")
    void deleteByUserId(int userId);
}
