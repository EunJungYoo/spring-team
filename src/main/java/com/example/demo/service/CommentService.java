package com.example.demo.service;

import com.example.demo.domain.dto.CommentRequestDto;
import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.validator.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthValidator authValidator;


    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository,
                          AuthValidator authValidator
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.authValidator = authValidator;
    }

    public ResponseDto<Object> getCommentList(Long id) {
        Post post = postRepository.findById(id).get();
        return ResponseDto.success(commentRepository.findAllByPost(post));
    }

    @Transactional
    public ResponseDto<Comment> postComment(CommentRequestDto commentRequestDto, Principal principal) {

        Post post = postRepository.findById(commentRequestDto.getPostId()).get();
        User user = userRepository.findByUserId(principal.getName());

        Comment comment = new Comment(commentRequestDto.getContent(), post, user);

        return ResponseDto.success(commentRepository.save(comment));
    }

    @Transactional
    public ResponseDto<Boolean> editComment(Long commentId, CommentRequestDto commentRequestDto, Principal principal) {

        Comment comment = commentRepository.findById(commentId).get();
        User user = comment.getUser();

        boolean isWriter = authValidator.isWriter(user, principal);

        if (isWriter) {
            return ResponseDto.success(comment.update(commentRequestDto));
        } else throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
    }

    @Transactional
    public ResponseDto<String> deleteComment(Long commentId, Principal principal) {
        Comment comment = commentRepository.findById(commentId).get();
        User user = comment.getUser();

        boolean isWriter = authValidator.isWriter(user, principal);

        if (isWriter) {
            commentRepository.delete(comment);
            return ResponseDto.success("댓글을 삭제했습니다.");
        } else throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
    }
}

// post와 comment 분리해서 생각한다면 service에 있는 코드들 더 간결하게 줄일 수 있음.