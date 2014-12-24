package com.xsw.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xsw.model.User;

/**
 * 
 * @author lyodssoft.com
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-10-10
 * @description 系统用户持久化接口 - 创建
 *
 */

public interface UserDao extends PagingAndSortingRepository<User, Integer>, JpaSpecificationExecutor<User> {

    // 返回用户条数判断用户是否已经存在
    @Query("select count(userId) from User where client.cid=?1 and userId!=?2 and loginName=?3")
    int findByUserForExists(int cid, int userId, String loginName);

    @Modifying
    @Query("update User u set u.status=?2, u.lockReason=?3 where u.loginName=?1")
    void saveUserStatusToLockAndLockReason(String loginName, int lock, int i);
}
