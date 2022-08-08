package com.example.demo.service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import com.example.demo.domain.Reply;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.CommentRequestDto;
import com.example.demo.domain.dto.ReplyRequestDto;
import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ReplyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.validator.AuthValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
public class ReplyService {
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final AuthValidator authValidator;

    public ReplyService(CommentRepository commentRepository, ReplyRepository replyRepository, UserRepository userRepository, AuthValidator authValidator) {
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.authValidator = authValidator;
    }

    //조회 관련
    public ResponseDto<Object> getReplyList(Long id) {
        Comment comment = commentRepository.findById(id).get();
        return ResponseDto.success(replyRepository.findAllByComment(comment));
    }

    //작성 관련
    @Transactional
    public ResponseDto<Reply> commentReply(ReplyRequestDto replyRequestDto, Principal principal) {

        Comment comment = commentRepository.findById(replyRequestDto.getCommentId()).get();
        User user = userRepository.findByUserId(principal.getName());

        Reply reply = new Reply(replyRequestDto.getContent(),comment,user);

        return ResponseDto.success(replyRepository.save(reply));
    }
    //수정 관련
    @Transactional
    public ResponseDto<Boolean> editReply(Long replyId, ReplyRequestDto replyRequestDto, Principal principal) {

//        Reply reply = replyRepository.findById(replyRequestDto.getCommentId()).get();
        Reply reply = replyRepository.findById(replyId).get();
        User user = reply.getUser();

        boolean isWriter = authValidator.isWriter(user, principal);

        if (isWriter) {
            return ResponseDto.success(reply.update(replyRequestDto));
        } else throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
    }
    //삭제 관련
    @Transactional
    public ResponseDto<String> deleteReply(Long replyId, Principal principal) {
        Reply reply = replyRepository.findById(replyId).get();
        User user = reply.getUser();

        boolean isWriter = authValidator.isWriter(user, principal);

        if (isWriter) {
            replyRepository.delete(reply);
            return ResponseDto.success("댓글을 삭제했습니다.");
        } else throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
    }
}

