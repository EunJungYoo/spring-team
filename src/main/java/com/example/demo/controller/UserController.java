package com.example.demo.controller;

import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.domain.dto.SignupRequestDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

    // 마이페이지
    @GetMapping("api/auth/users")
    public ResponseDto<?> getMyInfo(Principal principal){
        return userService.getMyInfo(principal);
    }

    // 회원 탈퇴
    @DeleteMapping("api/auth/users")
    public ResponseDto<String> deleteAccount(Principal principal){
        return userService.deleteAccount(principal);
    }

}



