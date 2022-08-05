package com.example.demo.domain.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    public String userId;
    public String username;
    public String password;
    public String passwordCheck;

}
