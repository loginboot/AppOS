package com.xsw.service;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.xsw.dao.AppListDao;
import com.xsw.model.AppList;

@Service
public class AppListService extends BaseService {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(AppListService.class);

    @Resource
    private AppListDao appListDao;

    /**
     * 根据查询条件进行系统应用列表
     * 
     * @param page
     * @param pageSize
     * @param search
     * @param sort
     * @return
     */
    @SuppressWarnings("unchecked")
    public Page<AppList> findByPage(int page, int pageSize, Map<String, Object> search, Sort sort) {
        log.debug("search AppList for page:[" + page + "] and pageSie:[" + pageSize + "]...");
        if (sort == null) {
            sort = new Sort(Direction.ASC, "rid");
        }
        PageRequest pageRequest = new PageRequest(page - 1, pageSize, sort);
        return appListDao.findAll((Specification<AppList>) buildSpecification(search, AppList.class), pageRequest);
    }

    public AppList findOne(int appId) {
        return appListDao.findOne(appId);
    }

    public void saveOrUpdate(AppList app, String[] mids) {

    }

    public void delete(int appId) {

    }

}
