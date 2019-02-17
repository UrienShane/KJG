package com.urien.kjg.service.impl;

import com.urien.kjg.dao.UserDao;
import com.urien.kjg.domain.User;
import com.urien.kjg.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service(value="userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findUserByName(String username){
        User user = userDao.findUserByName(username);
        return user;
    }

    @Override
    public User findUserByTelephoneNum(String telephoneNum){
        User user = userDao.findUserByTelephoneNum(telephoneNum);
        return user;
    }
}
