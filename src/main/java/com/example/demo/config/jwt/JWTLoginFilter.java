package com.example.demo.config.jwt;

import com.example.demo.config.exception.RestApiException;
import com.example.demo.domain.dto.LoginRequestDto;
import com.example.demo.domain.User;
import com.example.demo.config.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();
    private JWTUtil jwt = new JWTUtil();

    @Autowired
    PasswordEncoder passwordEncoder;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/users/signin");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest reqeust,
            HttpServletResponse response) throws AuthenticationException {

        String refreshToken = reqeust.getHeader("RefreshToken");

        LoginRequestDto userLogin = objectMapper.readValue(reqeust.getInputStream(), LoginRequestDto.class);

        if (refreshToken == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userLogin.getUserId(), userLogin.getPassword(), null);
            return getAuthenticationManager().authenticate(token);
        } else {

            VerifyResult verify = JWTUtil.verify(refreshToken);

            if (verify.isSuccess()) {
                UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                        userLogin.getUserId(), userLogin.getPassword(), null);

                return getAuthenticationManager().authenticate(userToken);
            } else {
                throw new IllegalArgumentException("Refresh token expired");
            }
        }
    }

    //자바 버전 오류로 인하여 추가한 문구
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        UserDetailsImpl user1 = (UserDetailsImpl) authResult.getPrincipal();
        User user = user1.getUser();


        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        response.setHeader("Refresh-Token", "refresh_token:" + jwt.makeRefreshToken(user));
        response.setHeader("Access-Token", "auth_token:" + jwt.makeAuthToken(user));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        response.getOutputStream().write(objectMapper.writeValueAsBytes(user.getUserId()));

    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {

        SecurityContextHolder.clearContext();

        RestApiException errorResponse = new RestApiException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다");
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(errorResponse));


    }


}