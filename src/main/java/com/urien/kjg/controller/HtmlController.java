package com.urien.kjg.controller;

import com.urien.kjg.domain.User;
import com.urien.kjg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HtmlController {

    /*
     * 测试数据库访问
     * */
    @Autowired
    private UserService userService;

    /*
    * 测试能否获取mongo数据以及dao是否正常
    * */
    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        User user = userService.findUserByName("admin");
        return user.getPassword();
    }

    /*
     * 测试thymeleaf
     * */
    @RequestMapping("/home")
    public String test(Model model){
        System.out.println("HtmlController.test()");
        model.addAttribute("name","这是主页");
        return "home";
    }

    @RequestMapping("/vipSee")
    public String vipSee(){
        System.out.println("HtmlController.vipSee()");
        return "user/vipSee";
    }

    @RequestMapping("/vipClick")
    public String vipClick(){
        System.out.println("HtmlController.vipClick()");
        return "user/vipClick";
    }

    @RequestMapping("/noAuth")
    public String noAuth(){
        System.out.println("HtmlController.noAuth()");
        return "noAuth";
    }
}
