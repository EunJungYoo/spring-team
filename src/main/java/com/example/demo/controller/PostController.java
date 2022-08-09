package com.example.demo.controller;

import com.example.demo.domain.Post;
import com.example.demo.domain.dto.PostRequestDto;
import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 전체 게시글 조회
    @GetMapping("/api/posts")
    public ResponseDto<?> getPostList(){
        return postService.getPostList();
    }

    // 게시글 상세 조회
    @GetMapping("/api/posts/{postId}")
    public ResponseDto<?> getPost(@PathVariable Long postId){
        return postService.getPost(postId);
    }

    // 게시글 작성
    @PostMapping("/api/auth/posts")
    public ResponseDto<Post> postPost(@RequestPart(value = "postInfo")
                                          PostRequestDto postRequestDto,
                                      @RequestPart(value = "file")
                                      MultipartFile multipartFile,
                                      Principal principal) throws IOException {
        return postService.postPost(postRequestDto, multipartFile, principal);
    }

    // 게시글 수정
    @PutMapping("/api/auth/posts/{id}")
    public ResponseDto<Post> editPost(@PathVariable Long id,
                                      @RequestBody PostRequestDto postRequestDto,
                                      Principal principal
    ){
        return postService.editPost(id, postRequestDto, principal);
    }

    // 게시글 삭제
    @DeleteMapping("/api/auth/posts/{id}")
    public ResponseDto<String> deletePost(@PathVariable Long id, Principal principal){
        return postService.deletePost(id, principal);
    }

    //좋아요 등록
    @GetMapping("/api/posts/addlikes/{id}")
    public String addLike(@PathVariable Long id, Principal principal) {
        return postService.addLike(id, principal);
    }

}
