package com.xsw.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xsw.model.AtLog;

public interface AtLogDao extends PagingAndSortingRepository<AtLog, Integer>, JpaSpecificationExecutor<AtLog> {

}
