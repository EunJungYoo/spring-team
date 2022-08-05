package com.example.demo.service.validator;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class AuthValidator {

    public boolean isWriter(User user, Principal principal) {
        String writerId = user.getUserId();
        String userId = principal.getName();
        return writerId.equals(userId);
    }

}
