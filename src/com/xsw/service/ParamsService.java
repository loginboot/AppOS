package com.xsw.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xsw.dao.ParamsDao;

@Service
public class ParamsService extends BaseService{

    @Resource
    private ParamsDao paramsDao;
}
