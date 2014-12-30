package com.xsw.dao;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.xsw.model.AtSession;

/**
 * 
 * @author lyodssoft.com
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-10-10
 * @description 系统在线用户登记持久化接口 - 创建
 *
 */

public interface AtSessionDao extends CrudRepository<AtSession, Integer> {

    @Query("from AtSession where lastOpTime<=?1")
    List<AtSession> findByLastOpTime(String optime);

    @Modifying
    @Query("delete from AtSession where lastOpTime<=?1")
    void deleteTimeoutSession(String lastAccessTime);

    @Modifying
    @Query("delete from AtSession where userId<=?1 and sessionId=?2")
    void deleteByUidAndSessionId(int uid, String sessionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    AtSession findOne(Integer userId);
}
