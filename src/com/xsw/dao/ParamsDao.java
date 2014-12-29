package com.xsw.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xsw.model.Params;

public interface ParamsDao extends PagingAndSortingRepository<Params, Integer>, JpaSpecificationExecutor<Params> {

}
