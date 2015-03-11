package com.xsw.service;

import java.util.ArrayList;
import java.util.List;
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
import com.xsw.dao.AppMenuDao;
import com.xsw.model.AppList;
import com.xsw.model.AppMenu;
import com.xsw.model.Menu;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw  
 * @version 1.0.0
 * @date 2015-01-04
 * @description 系统应用 - 服务操作
 *
 */

@Service
public class AppListService extends BaseService {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(AppListService.class);

    @Resource
    private AppListDao appListDao;

    @Resource
    private AppMenuDao appMenuDao;

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
            sort = new Sort(Direction.ASC, "appId");
        }
        PageRequest pageRequest = new PageRequest(page - 1, pageSize, sort);
        return appListDao.findAll((Specification<AppList>) buildSpecification(search, AppList.class), pageRequest);
    }

    /**
     * 返回一条应用信息
     * @param appId
     * @return
     */
    public AppList findOne(int appId) {
        return appListDao.findOne(appId);
    }

    /**
     * 新增或修改应用信息
     * @param app
     * @param mids
     */
    public void saveOrUpdate(AppList app, String[] mids) {
        log.debug("save or update app info:[" + app + "]...");
        AppList tmp = appListDao.save(app);
        if (app.getAppId() != 0) {
            appMenuDao.deleteByAppId(app.getAppId());
        }
        if (mids != null) {
            List<AppMenu> amlst = new ArrayList<AppMenu>();
            for (String mid : mids) {
                AppMenu am = new AppMenu();
                Menu m = new Menu();
                m.setMid(Integer.parseInt(mid));
                am.setMenu(m);
                am.setAppList(tmp);
                amlst.add(am);
            }
            appMenuDao.save(amlst);
        }
    }

    /**
     * 删除应用记录
     * @param appId
     */
    public void delete(int appId) {
        log.debug("delete app for appId[" + appId + "]...");
        appMenuDao.deleteByAppId(appId);
        appListDao.delete(appId);
    }

}
