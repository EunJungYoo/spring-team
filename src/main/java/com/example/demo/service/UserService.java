package com.example.demo.service;

import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.domain.dto.SignupRequestDto;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.validator.UserSignupValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSignupValidator userSignupValidator;


    @Autowired
    public UserService(UserRepository userRepository, UserSignupValidator userSignupValidator) {
        this.userRepository = userRepository;
        this.userSignupValidator = userSignupValidator;
    }

    @Transactional
    public ResponseDto<Boolean> checkIDDuplicate(String userId) {
        return ResponseDto.success(userRepository.existsByUserId(userId));
    }

    @Transactional
    public void signUp(SignupRequestDto requestDto) {
        boolean isIdDuplicate = checkIDDuplicate(requestDto.getUserId()).getData();

        User user = userSignupValidator.validateSignupRequest(requestDto, isIdDuplicate);
        userRepository.save(user);
    }

}