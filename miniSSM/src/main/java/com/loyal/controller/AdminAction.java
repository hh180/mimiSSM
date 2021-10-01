package com.loyal.controller;

import com.loyal.pojo.Admin;
import com.loyal.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminAction {
    //注入业务逻辑层对象
    @Autowired
    AdminService adminService;

    //实现登录判断，并进行跳转
    @RequestMapping("/login")
    public ModelAndView login(String name, String pwd, HttpServletRequest request){
        Admin admin = adminService.login(name, pwd);
        ModelAndView modelAndView = new ModelAndView();
        if(admin!=null){
            //登录成功
            modelAndView.addObject("admin",admin);
            modelAndView.setViewName("main");
            return modelAndView;
        }else {
            //登录失败
           modelAndView.addObject("errmsg","用户名或密码输入错误");
           modelAndView.setViewName("login");
           return modelAndView;
        }
    }
}
