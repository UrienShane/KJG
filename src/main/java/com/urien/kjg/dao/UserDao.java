package com.urien.kjg.dao;

import com.urien.kjg.domain.User;

public interface UserDao {

    User findUserByName(String username);

    User findUserByTelephoneNum(String telephoneNum);

    void insertOneUser(User user);

//    void insertOneUser(String telephoneNum, String passWord);

}
