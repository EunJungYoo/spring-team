package com.example.demo.service;

import com.example.demo.domain.LikeDomain.PostLike;
import com.example.demo.domain.dto.LikeResponseDto;
import com.example.demo.domain.dto.PostRequestDto;
import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.likeDto.PostLikeDto;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.SelectJPAColumnInterface.ShowPost;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.likeRepository.PostLikeRepository;
import com.example.demo.domain.imageDomain.AwsS3;
import com.example.demo.service.fileUpload.AmazonS3Service;
import com.example.demo.service.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final AuthValidator authValidator;
    private final AmazonS3Service amazonS3Service;
    private final String amazonS3Domain = "https://springblog.s3.ap-northeast-2.amazonaws.com/";

    @Transactional
    public ResponseDto<?> getPostList() {
        int count = 0;
        List<ShowPost> postList = postRepository.findAllByOrderByCreatedAtDesc();

        for(ShowPost post : postList){
            count += post.getLikeCount();
        }
        LikeResponseDto responseDto = new LikeResponseDto(postList, count);

        return ResponseDto.success(responseDto);
    }

    @Transactional
    public ResponseDto<?> getPost(Long id) {
        Post post = postRepository.findById(id).get();
        return ResponseDto.success(post);
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

    @Transactional
    public String addLike(Long id, Principal principal) {
        //포스트 레포에서 아이디값을 기준으로 포스트를 가져와서 post라고 값을 선언 -> 포스트 아이디를 가져옴
        Post post = postRepository.findById(id).get();
        //유저 레포에서 String으로 된 유저아이디를 가져와서 user라는 변수명으로 씀 -> 유저아이디를 가져옴
        User user = userRepository.findByUserId(principal.getName());
        //포스트라이크 디티오를 위에서 변수명 정한걸로 넣어서 새로 만들고 postLikeDto라고 값을 저장함 -> postLikeDto 인스턴스를 만듬
        PostLikeDto postLikeDto = new PostLikeDto(user, post);
        //포스트라이크에 포스트라이크디티오를 넣어서 새로운 객체를 만들고 postLike에 값을 넣어줌
        PostLike postLike = new PostLike(postLikeDto);
        //테이블에 값이 있으면 좋아요취소, 값이 없으면 좋아요 등록
        if(postLikeRepository.findByPostAndUser(post, user).isPresent()) {
            //포스트라이크레포에 delete (이건 정해져있는거임) 함수를 써서 postLike를 삭제함. 근데 조건이 맞는 포스트아이디랑, 내가쓴글인지 확인하는 유저에 따라 찾아와야함. 그래서 .dele~~를 썼는데 그걸 postLikeRepository에서 정의함
            postLikeRepository.deleteByPostAndUser(post, user);
            //포스트클래스에 deleteLike라는 함수를 가져옴 파라미터에 postLikeDto를 넣음 이건 post가서 다시 봐 준철아.
            post.deleteLike(postLikeDto);
            return "좋아요 취소되었습니다.";
        }
        post.addLike(postLikeDto);
        postLikeRepository.save(postLike);
        return "좋아요 등록되었습니다.";

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


