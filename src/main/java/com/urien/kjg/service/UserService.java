package com.urien.kjg.service;

import com.urien.kjg.domain.User;
import org.springframework.ui.Model;

public interface UserService {

    User findUserByName(String username);

    User findUserByTelephoneNum(String telephoneNum);

}
