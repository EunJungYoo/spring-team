package com.example.demo.service;


import com.example.demo.domain.LikeDomain.CommentLike;
import com.example.demo.domain.dto.CommentRequestDto;
import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.likeDto.CommentLikeDto;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.likeRepository.CommentLikeRepository;
import com.example.demo.service.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository; //포스트 아이디 갖고오는
    private final UserRepository userRepository; //유저 아이디 갖고오는->String임
    private final AuthValidator authValidator; //토큰확인인듯


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
        Post post = postRepository.findById(comment.getPost().getPostId()).get();
        User user = comment.getUser();

        post.setCommentCount(post.getCommentCount() - 1);

        boolean isWriter = authValidator.isWriter(user, principal);

        if (isWriter) {
            commentRepository.delete(comment);
            return ResponseDto.success("댓글을 삭제했습니다.");
        } else throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
    }

    @Transactional
    public String addLike(Long id, Principal principal) {
        Comment comment = commentRepository.findById(id).get();
        User user = userRepository.findByUserId(principal.getName());

        CommentLikeDto commentLikeDto = new CommentLikeDto(user, comment);
        CommentLike commentLike = new CommentLike(commentLikeDto);

        if (commentLikeRepository.findByCommentAndUser(comment, user).isPresent()) {
            commentLikeRepository.deleteByCommentAndUser(comment, user);
            comment.deleteLike(commentLikeDto);

            return "좋아요 취소되었습니다.";
        }

        comment.addLike(commentLikeDto);
        commentLikeRepository.save(commentLike);

        return "좋아요 등록되었습니다.";
    }
}

// post와 comment 분리해서 생각한다면 service에 있는 코드들 더 간결하게 줄일 수 있음.