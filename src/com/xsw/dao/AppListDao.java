package com.xsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xsw.model.AppList;

public interface AppListDao extends PagingAndSortingRepository<AppList, Integer>, JpaSpecificationExecutor<AppList> {

    
    List<AppList> findByStatus(int status);
}
