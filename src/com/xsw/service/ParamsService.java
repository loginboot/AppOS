package com.xsw.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xsw.dao.ParamsDao;
import com.xsw.model.Params;

@Service
public class ParamsService extends BaseService {

    @Resource
    private ParamsDao paramsDao;

    /**
     * 根据应用返回对应参数信息
     * @param appId
     * @return
     */
    public List<Params> findParamsByAppId(int appId) {
        return paramsDao.findParamsByAppId(appId);
    }

    /**
     * 保存参数
     * 
     * @param params
     */
    public void saveParams(List<Params> params) {
        paramsDao.save(params);
    }
}
