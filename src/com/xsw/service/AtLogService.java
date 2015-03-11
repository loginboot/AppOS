package com.xsw.service;

import java.util.LinkedHashMap;
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

import com.xsw.dao.AtLogDao;
import com.xsw.dao.MenuDao;
import com.xsw.dao.UserDao;
import com.xsw.model.AtLog;
import com.xsw.model.Menu;
import com.xsw.model.User;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2015-01-02
 * @description 系统操作日志 
 *
 */

@Service
public class AtLogService extends BaseService {
    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(AtLogService.class);

    @Resource
    private AtLogDao atLogDao;

    @Resource
    private UserDao userDao;
    @Resource
    private MenuDao menuDao;

    /**
     * 返回日志信息列表
     * @param page
     * @param pageSize
     * @param search
     * @param sort
     * @return
     */
    @SuppressWarnings("unchecked")
    public Page<AtLog> findByPage(int page, int pageSize, Map<String, Object> search, Sort sort) {
        log.debug("***search AtLog by Page for page:[" + page + "] and pageSize:[" + pageSize + "]***");
        if (sort == null) {
            sort = new Sort(Direction.DESC, "operTime");
        }
        PageRequest pageRequest = new PageRequest(page - 1, pageSize, sort);
        return atLogDao.findAll((Specification<AtLog>) buildSpecification(search, AtLog.class), pageRequest);
    }

    /**
     * 返回用户名称
     * 
     * @param userId
     * @return
     */
    public String getUserNameByLogId(int userId) {
        User user = userDao.findOne(userId);
        if (user == null) {
            return "";
        }
        return user.getName();
    }

    /**
     * 返回对应的菜单名称ID信息
     * 
     * @param permission
     * @return
     */
    public Map<String, String> getMenuByType(int type) {
        Sort sort = new Sort(Direction.ASC, "mid");
        List<Menu> ms = (List<Menu>) menuDao.findAll(sort);
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("system", "MENU.01000");
        for (Menu m : ms) {
            if (type == m.getType()) {
                map.put(m.getPermission(), m.getName());
            }
        }
        map.put("exception", "PUB.exception");
        return map;
    }

    /**
     * 返回菜单名称信息
     * 
     * @param permission
     * @return
     */
    public String getMenuNameByPermission(String permission) {
        List<Menu> ms = menuDao.findByPermission(permission);
        if (ms.size() == 0) {
            return "";
        }
        return ms.get(0).getName();
    }

}
