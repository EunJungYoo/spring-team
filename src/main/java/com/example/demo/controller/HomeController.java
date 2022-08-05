package com.example.demo.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public String index(){
        return "redirect:/index.html";
    }

    // 기본적으로 spring은 '/'로 접속했을 때 index.html 파일을 띄움.

}
