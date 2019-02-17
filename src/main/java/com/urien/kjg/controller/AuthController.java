package com.urien.kjg.controller;

import com.urien.kjg.domain.User;
import com.urien.kjg.service.UserService;
import com.urien.kjg.util.IsMobileUtil;
import com.urien.kjg.util.PasswordUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordUtil passwordUtil;


    @RequestMapping("/toLogin")
    public String toLogin() {
        System.out.println("AuthController.toLogin()");
        return "login";
    }

    /*
     * 登录逻辑处理
     * */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("account") String account, @RequestParam("password") String password, Model model) {
        System.out.println("---AuthController得到的数据---");
        System.out.println("account = " + account);
        System.out.println("password = " + password);

        //1.获取Subject
        Subject subject = SecurityUtils.getSubject();
            //将用户名及密码封装交给UsernamePasswordToken
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);

        //2.判断当前用户是否登录
        try {
            subject.login(token);
            return "redirect:/home";
        } catch (UnknownAccountException e) {
            model.addAttribute("login", "账户不存在");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("login", "密码错误");
            return "login";
        } catch (AuthenticationException e) {
            //登录失败
            model.addAttribute("login", "登录异常");
            return "login";
        }
    }
}
