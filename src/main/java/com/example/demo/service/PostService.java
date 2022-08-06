package com.example.demo.service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.dto.PostRequestDto;
import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ShowPost;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.validator.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthValidator authValidator;



    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, AuthValidator authValidator) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.authValidator = authValidator;
    }


    @Transactional
    public ResponseDto<?> getPostList() {
        List<ShowPost> postList = postRepository.findAllByOrderByCreatedAtDesc();
        return ResponseDto.success(postList);
    }

    @Transactional
    public ResponseDto<?> getPost(Long id) {
        Post postList = postRepository.findById(id).get();
        return ResponseDto.success(postList);
    }

    @Transactional
    public ResponseDto<Post> postPost(PostRequestDto postRequestDto, Principal principal) {
        User user = userRepository.findByUserId(principal.getName());

        Post post = new Post(postRequestDto, user);
        return ResponseDto.success(postRepository.save(post));
    }


    @Transactional
    public ResponseDto<Post> editPost(Long id, PostRequestDto postRequestDto, Principal principal) {
        Post post = postRepository.findById(id).get();
        User user = post.getUser();

        if (authValidator.isWriter(user, principal)) {
            post.update(postRequestDto);
            return ResponseDto.success(post);

        } else throw new IllegalArgumentException("게시물을 수정할 권한이 없습니다.");

    }


    @Transactional
    public ResponseDto<String> deletePost(Long id, Principal principal) {
        Post post = postRepository.findById(id).get();
        User user = post.getUser();

        if (authValidator.isWriter(user, principal)) {
            postRepository.delete(post);
            return ResponseDto.success("게시물이 삭제되었습니다");

        } else throw new IllegalArgumentException("게시물을 삭제할 권한이 없습니다.");

    }

}
