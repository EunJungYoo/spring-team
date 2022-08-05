package com.example.demo.controller;

import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.domain.dto.SignupRequestDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 아이디 중복확인
    @GetMapping("api/users/signup/{userId}")
    public ResponseDto<Boolean> checkIDDuplicate(@PathVariable String userId){
        return userService.checkIDDuplicate(userId);
    }

    // 회원가입
    @PostMapping("api/users/signup")
    public String signUp(@RequestBody SignupRequestDto signupRequestDto){
        userService.signUp(signupRequestDto);
        return "redirect:/index.html";
    }
}



