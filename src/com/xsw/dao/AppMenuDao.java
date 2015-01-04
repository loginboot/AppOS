package com.xsw.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.xsw.model.AppMenu;

public interface AppMenuDao extends CrudRepository<AppMenu, Integer> {

    // 根据应用ID删除所有关联菜单
    @Modifying
    @Query("delete from AppMenu where appList.appId=?1")
    void deleteByAppId(int appId);

}
