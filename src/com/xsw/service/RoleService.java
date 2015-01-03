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

import com.xsw.dao.RoleDao;
import com.xsw.dao.RoleMenuDao;
import com.xsw.model.Menu;
import com.xsw.model.Role;
import com.xsw.model.RoleMenu;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw  
 * @version 1.0.0
 * @date 2015-01-03
 * @description 系统角色信息 - 服务处理
 *
 */

public class RoleService extends BaseService {
    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(RoleService.class);

    @Resource
    private RoleDao roleDao;

    @Resource
    private RoleMenuDao roleMenuDao;

    /**
     * 根据查询条件进行系统角色列表
     * 
     * @param page
     * @param pageSize
     * @param search
     * @param sort
     * @return
     */
    @SuppressWarnings("unchecked")
    public Page<Role> findByPage(int page, int pageSize, Map<String, Object> search, Sort sort) {
        log.debug("search role for page:[" + page + "] and pageSie:[" + pageSize + "]...");
        if (sort == null) {
            sort = new Sort(Direction.ASC, "rid");
        }
        PageRequest pageRequest = new PageRequest(page - 1, pageSize, sort);
        return roleDao.findAll((Specification<Role>) buildSpecification(search, Role.class), pageRequest);
    }

    /**
     * 根据角色ID返回角色记录
     * @param rid
     * @return
     */
    public Role findOne(int rid) {
        return roleDao.findOne(rid);
    }

    /**
     * 保存或更新角色信息
     * @param role
     * @param mids
     */
    public void saveOrUpdate(Role role, String[] mids) {
        Role tmp = roleDao.save(role);
        if (role.getRid() != 0) {
            roleMenuDao.deleteByRid(role.getRid());
        }
        // 是否有权限信息
        if (mids != null) {
            List<RoleMenu> rmlst = new ArrayList<RoleMenu>();
            for (String mid : mids) {
                RoleMenu rm = new RoleMenu();
                rm.setRole(tmp);
                Menu m = new Menu();
                m.setMid(Integer.parseInt(mid));
                rm.setMenu(m);
                rmlst.add(rm);
            }
            roleMenuDao.save(rmlst);
        }
    }

    /**
     * 根据角色ID删除角色信息
     * @param rid
     */
    public void delete(int rid) {
        log.debug("delete one role for rid:[" + rid + "]...");
        roleMenuDao.deleteByRid(rid);
        roleDao.delete(rid);
    }

}
