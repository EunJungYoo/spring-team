package com.example.demo.controller;

import com.example.demo.domain.dto.CommentRequestDto;
import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.domain.Comment;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 목록 조회
    @GetMapping("/api/comments/{postId}")
    public ResponseDto<Object> getCommentList(@PathVariable Long postId) {
        return commentService.getCommentList(postId);
    }

    // 댓글 등록
    @PostMapping("/api/auth/comments")
    public ResponseDto<Comment> postComment(@RequestBody CommentRequestDto commentRequestDto, Principal principal){
        return commentService.postComment(commentRequestDto, principal);
    }

   // 댓글 수정
    @PutMapping("/api/auth/comments/{commentId}")
    public ResponseDto<Boolean> editComment(@PathVariable Long commentId,
                                            @RequestBody CommentRequestDto commentRequestDto,
                                            Principal principal){
        return commentService.editComment(commentId, commentRequestDto, principal);
    }

    // 댓글 삭제
    @DeleteMapping("/api/auth/comments/{commentId}")
    public ResponseDto<String> deleteComment(@PathVariable Long commentId, Principal principal){
        return commentService.deleteComment(commentId, principal);
    }

    //좋아요 등록, 삭제 ->get



}
