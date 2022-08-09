package com.example.demo.controller;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Reply;
import com.example.demo.domain.dto.CommentRequestDto;
import com.example.demo.domain.dto.ReplyRequestDto;
import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class ReplyController {
    //Reply컨트롤러와 Reply서비스를 연결 시켜주는 느낌?
    private final ReplyService replyService;
    //생성자 생성 (Entity가 아니기때문에 기본생성자까지는 안만들어도 된다?)
    @Autowired //로딩된 객체를 해당 변수에 담아준다?)
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    //대댓글 조회
    @GetMapping("/api/replys/{postId}")
    public ResponseDto<Object> getCommentList(@PathVariable Long postId) {
        return replyService.getReplyList(postId);
    }

    //대댓글 작성
    @PostMapping("/api/auth/replys")
    public ResponseDto<Reply> commentReply(@RequestBody ReplyRequestDto replyRequestDto, Principal principal){
        return replyService.commentReply(replyRequestDto, principal);
    }
    //대댓글 수정
    @PutMapping("/api/auth/replys/{replyId}")
    public ResponseDto<Boolean> editReply(@PathVariable Long replyId,
                                            @RequestBody ReplyRequestDto replyRequestDto,
                                            Principal principal){
        return replyService.editReply(replyId, replyRequestDto, principal);
    }

    //대댓글 삭제
    @DeleteMapping("/api/auth/replys/{replyId}")
    public ResponseDto<String> deleteReply(@PathVariable Long replyId, Principal principal){
        return replyService.deleteReply(replyId, principal);
    }

    //좋아요 등록
    @GetMapping("/api/auth/reply/like/{id}")
    public String addLike(@PathVariable Long id, Principal principal) {
        return replyService.addLike(id, principal);
    }



}
