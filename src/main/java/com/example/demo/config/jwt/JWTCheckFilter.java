package com.example.demo.config.jwt;

import com.example.demo.config.exception.RestApiException;
import com.example.demo.domain.User;
import com.example.demo.config.security.UserDetailsImpl;
import com.example.demo.config.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTCheckFilter extends BasicAuthenticationFilter {

    private UserDetailsServiceImpl userDetailsServiceImpl;
    private ObjectMapper objectMapper = new ObjectMapper();

    public JWTCheckFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpl) {
        super(authenticationManager);
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearer = request.getHeader("Access-Token");

       if(bearer == null || bearer.startsWith("auth_token ")){
            chain.doFilter(request,response);
            return;
        }
       String token = bearer.substring("auth_token:".length());

       VerifyResult result = JWTUtil.verify(token);

       if(result.isSuccess()){
           UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(result.getUserId());
           User user = userDetailsImpl.getUser();

           UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                   user.getUserId(), null, user.getAuthorities()
           );

           SecurityContextHolder.getContext().setAuthentication(userToken);
           chain.doFilter(request,response);
       } else {
           //에러메세지와 Status 변경
           RestApiException errorResponse = new RestApiException(HttpStatus.OK, "로그인이 필요합니다.");
           response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
           response.getOutputStream().write(objectMapper.writeValueAsBytes(errorResponse));
       }

    }
}
