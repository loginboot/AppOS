package com.xsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xsw.model.Params;

public interface ParamsDao extends PagingAndSortingRepository<Params, Integer>, JpaSpecificationExecutor<Params> {

    // 返回应用系统参数
    @Query("from Params where appList.appId=?1")
    List<Params> findParamsByAppId(int appId);
}
