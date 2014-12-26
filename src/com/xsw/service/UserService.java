package com.xsw.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.xsw.dao.UserDao;
import com.xsw.model.User;

@Service
public class UserService extends BaseService {

    @Resource
    private UserDao userDao;

    /**
     * 根据查询条件进行系统用户列表
     * 
     * @param page
     * @param pageSize
     * @param search
     * @param sort
     * @return
     */
    @SuppressWarnings("unchecked")
    public Page<User> findByPage(int page, int pageSize, Map<String, Object> search, Sort sort) {
        if (sort == null) {
            sort = new Sort(Direction.ASC, "userId");
        }
        PageRequest pageRequest = new PageRequest(page - 1, pageSize, sort);
        return userDao.findAll((Specification<User>) buildSpecification(search, User.class), pageRequest);
    }

    /**
     * 根据用户ID返回一条用户记录
     * 
     * @param userId
     * @return
     */
    public User findOne(int userId) {
        return userDao.findOne(userId);
    }

    public void saveOrUpdate(User user,int rid){
        User tmp = userDao.save(user);
        
        
        
    }
}
