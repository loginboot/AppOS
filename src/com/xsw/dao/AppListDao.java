package com.xsw.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xsw.model.AppList;

public interface AppListDao extends PagingAndSortingRepository<AppList, Integer>, JpaSpecificationExecutor<AppList> {

}
