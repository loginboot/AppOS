package com.xsw.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xsw.dao.AppListDao;
import com.xsw.dao.AppMenuDao;
import com.xsw.dao.MenuDao;
import com.xsw.model.AppList;
import com.xsw.model.AppMenu;
import com.xsw.model.Menu;

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
    public List<AppMenu> findByAppId(int appId){
        return appMenuDao.findByAppId(appId);
    }

}
