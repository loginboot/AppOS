package com.xsw.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xsw.dao.AppListDao;
import com.xsw.dao.AppMenuDao;
import com.xsw.dao.MenuDao;
import com.xsw.model.AppList;
import com.xsw.model.AppMenu;
import com.xsw.model.Menu;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2015-01-10
 * @description 菜单服务 - 创建
 *
 */

@Service
public class MenuService {

    @Resource
    private MenuDao menuDao;

    @Resource
    private AppListDao appListDao;

    @Resource
    private AppMenuDao appMenuDao;

    /**
     * 返回所有系统菜单
     * @return
     */
    public List<Menu> findBySystemMenu() {
        return menuDao.findSystemMenu();
    }

    /**
     * 返回所有应用信息
     * @return
     */
    public List<AppList> findAllApp() {
        return (List<AppList>) appListDao.findAll();
    }

    /**
     * 返回对应的应用菜单信息
     * @param appId
     * @return
     */
    public List<AppMenu> findByAppId(int appId) {
        return appMenuDao.findByAppId(appId);
    }

    /**
     * 保存修改菜单
     * @param mids
     * @param appId
     */
    public void saveOrUpdate(String[] mids, int appId) {
        if (mids != null) {
            // 先删除已经存在的菜单
            appMenuDao.deleteByAppId(appId);
            // 新增关联信息
            List<AppMenu> amlst = new ArrayList<AppMenu>();
            for (String mid : mids) {
                AppMenu am = new AppMenu();
                Menu m = new Menu();
                m.setMid(Integer.parseInt(mid));
                AppList app = new AppList();
                app.setAppId(appId);
                am.setAppList(app);
                am.setMenu(m);
                amlst.add(am);
            }
            appMenuDao.save(amlst);
        }
    }

}
