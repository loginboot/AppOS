package com.xsw.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xsw.dao.UserDao;

@Service
public class LoginService {
    
    @Resource
    private UserDao userDao;

}
