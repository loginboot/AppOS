package com.xsw.service;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.xsw.utils.DynamicSpecifications;
import com.xsw.utils.SearchFilter;

public class BaseService {
    
    /**
     * 创建动态查询条件组合.
     */
    public Specification<?> buildSpecification(Map<String, Object> searchParams, Class<?> cls) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<?> spec = DynamicSpecifications.bySearchFilter(filters.values(), cls);
        return spec;
    }
}
