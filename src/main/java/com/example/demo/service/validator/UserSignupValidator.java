package com.example.demo.service.validator;

import com.example.demo.domain.User;
import com.example.demo.domain.dto.SignupRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserSignupValidator {


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserSignupValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean pwCheck(String password) {
        String pwPattern = "^[a-z|0-9]*$";

        boolean patternCheck = Pattern.matches(pwPattern, password);
        boolean lenCheck = password.length() >= 4 && password.length() <= 32;
        return patternCheck && lenCheck;
    }

    public boolean idCheck(String userId) {
        String idPattern = "^[a-z|A-Z|0-9]*$";

        boolean patternCheck = Pattern.matches(idPattern, userId);
        boolean lenCheck = userId.length() >= 4 && userId.length() <= 12;
        return patternCheck && lenCheck;
    }

    public User validateSignupRequest(SignupRequestDto requestDto, boolean isIdDuplicate){
        String username = requestDto.getUsername();
        String userId = requestDto.getUserId();
        String password = requestDto.getPassword();
        String passwordCheck = requestDto.getPasswordCheck();
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());


        if (isIdDuplicate) {
            throw new IllegalArgumentException("중복된 사용자 ID입니다");
        }
        if (!passwordCheck.equals(password)) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다");
        }
        if (!idCheck(userId)) {
            throw new IllegalArgumentException("아이디 형식을 확인해주세요");
        }
        if (!pwCheck(password)) {
            throw new IllegalArgumentException("비밀번호 형식을 확인해주세요");
        }

        User user = new User(userId, username, encodedPassword);
        return user;


    }


}