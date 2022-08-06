package com.example.demo.controller;

import com.example.demo.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplyController {
    //Reply컨트롤러와 Reply서비스를 연결 시켜주는 느낌?
    private final ReplyService replyService;
    //생성자 생성 (Entity가 아니기때문에 기본생성자까지는 안만들어도 된다?)
    @Autowired //로딩된 객체를 해당 변수에 담아준다?)
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }
//    @GetMapping("/api/comments/{postId}")

}
