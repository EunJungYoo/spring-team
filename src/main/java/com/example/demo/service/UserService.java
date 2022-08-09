package com.example.demo.service;

import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.domain.dto.SignupRequestDto;
import com.example.demo.domain.User;
import com.example.demo.repository.SelectJPAColumnInterface.ShowMypage;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.validator.UserSignupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSignupValidator userSignupValidator;

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

    @Transactional
    public ResponseDto<?> getMyInfo(Principal principal) {
        User user = userRepository.findByUserId(principal.getName());
        ShowMypage user1 = userRepository.findByUsernameAndUserId(user.getUsername(),user.getUserId());
        return ResponseDto.success(user1);
    }

    @Transactional
    public ResponseDto<String> deleteAccount(Principal principal) {
        userRepository.deleteByUserId(principal.getName());
        return ResponseDto.success("계정이 삭제되었습니다.");
    }

}