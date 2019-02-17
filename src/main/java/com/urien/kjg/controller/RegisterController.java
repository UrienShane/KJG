package com.urien.kjg.controller;

import com.urien.kjg.dao.UserDao;
import com.urien.kjg.domain.User;
import com.urien.kjg.service.UserService;
import com.urien.kjg.util.IsMobileUtil;
import com.urien.kjg.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterController {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordUtil passwordUtil;


    @RequestMapping("/toRegister")
    public String toRegister() {
        System.out.println("AuthController.toRegister()");
        return "register";
    }

    /*
     * 注册逻辑处理
     * */
    @RequestMapping("/register")
    public String register(String telephoneNum, String password, String identifyingCode, Model model){
        System.out.println("---RegisterController得到的数据---");
        System.out.println("telephoneNum = " + telephoneNum);
        System.out.println("password = " + password);


        if(telephoneNum != null && password != null && identifyingCode !=null){

            //对密码进行加密处理
            Object password_encoded = passwordUtil.encodePassword(password, telephoneNum);
            System.out.println("加密后的密码 = " + password_encoded);

            identifyingCode = "0";

            if(identifyingCode == "0"){
                User user = userDao.findUserByTelephoneNum(telephoneNum);
                if(user == null){
                    User user_new = new User();
                    user_new.setTelephoneNum(telephoneNum);
                    user_new.setPassword((String) password_encoded);
                    userDao.insertOneUser(user_new);
                    model.addAttribute("register", "注册成功");
                    return "login";
                }else {
                    model.addAttribute("register", "该手机号已被注册");
                    return "register";
                }
            }else {
                model.addAttribute("register", "验证码错误");
                return "register";
            }
        }else if(telephoneNum == null){
            model.addAttribute("register", "手机号不能为空");
            return "register";
        }else if(password == null){
            model.addAttribute("register", "密码不能为空");
            return "register";
        }else if(identifyingCode == null){
            model.addAttribute("register", "请输入验证码");
            return "register";
        }

        return null;
    }
}
