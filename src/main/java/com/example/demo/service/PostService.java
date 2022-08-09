package com.example.demo.service;

import com.example.demo.domain.LikeDomain.PostLike;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.PostRequestDto;
import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.domain.dto.likeDto.PostLikeDto;
import com.example.demo.domain.imageDomain.AwsS3;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ShowPost;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.likeRepo.PostLikeRepository;
import com.example.demo.service.fileUpload.AmazonS3Service;
import com.example.demo.service.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthValidator authValidator;
    private final AmazonS3Service amazonS3Service;

    private final PostLikeRepository postLikeRepository;
    private final String amazonS3Domain = "https://springblog.s3.ap-northeast-2.amazonaws.com/";



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
    public ResponseDto<Post> postPost(PostRequestDto postRequestDto,
                                      MultipartFile multipartFile, Principal principal) throws IOException {
        User user = userRepository.findByUserId(principal.getName());
        AwsS3 image = amazonS3Service.upload(multipartFile, "upload");

        String imageUrl = amazonS3Domain + URLEncoder.encode(image.getKey(), StandardCharsets.US_ASCII);

        postRequestDto.setImageUrl(imageUrl);
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


    /*
        매일 오전 1시
        댓글이 하나도 없는 게시글 삭제하는 스케줄러
     */
    @Scheduled(cron = "0 0 1 * * *") // 오전 1시
    public void removePost() throws InterruptedException {
        List<Post> allPosts = postRepository.findAll();

        for (Post post : allPosts) {
            TimeUnit.SECONDS.sleep(1);
            if (post.getCommentList().size() == 0) {
                postRepository.delete(post);
                log.info("게시물 <{}>이 삭제되었습니다.", post.getTitle());
            }
        }
    }


}