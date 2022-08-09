package com.example.demo.service;

import com.example.demo.domain.LikeDomain.CommentLike;
import com.example.demo.domain.LikeDomain.PostLike;
import com.example.demo.domain.dto.CommentRequestDto;
import com.example.demo.domain.dto.ResponseDto;
import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.likeDto.CommentLikeDto;
import com.example.demo.domain.dto.likeDto.PostLikeDto;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.likeRepository.CommentLikeRepository;
import com.example.demo.service.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

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
        User user = comment.getUser();

        boolean isWriter = authValidator.isWriter(user, principal);

        if (isWriter) {
            commentRepository.delete(comment);
            return ResponseDto.success("댓글을 삭제했습니다.");
        } else throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
    }

    @Transactional
    public String addLike(Long id, Principal principal) {
        //포스트 레포에서 아이디값을 기준으로 포스트를 가져와서 post라고 값을 선언 -> 포스트 아이디를 가져옴
        Comment comment = commentRepository.findById(id).get();
        //유저 레포에서 String으로 된 유저아이디를 가져와서 user라는 변수명으로 씀 -> 유저아이디를 가져옴
        User user = userRepository.findByUserId(principal.getName());
        //포스트라이크 디티오를 위에서 변수명 정한걸로 넣어서 새로 만들고 postLikeDto라고 값을 저장함 -> postLikeDto 인스턴스를 만듬
        CommentLikeDto commentLikeDto = new CommentLikeDto(user, comment);
        //포스트라이크에 포스트라이크디티오를 넣어서 새로운 객체를 만들고 postLike에 값을 넣어줌
        CommentLike commentLike = new CommentLike(commentLikeDto);
        //테이블에 값이 있으면 좋아요취소, 값이 없으면 좋아요 등록
        if (commentLikeRepository.findByCommentAndUser(comment, user).isPresent()) {
            //포스트라이크레포에 delete (이건 정해져있는거임) 함수를 써서 postLike를 삭제함. 근데 조건이 맞는 포스트아이디랑, 내가쓴글인지 확인하는 유저에 따라 찾아와야함. 그래서 .dele~~를 썼는데 그걸 postLikeRepository에서 정의함
            commentLikeRepository.deleteByCommentAndUser(comment, user);
            //포스트클래스에 deleteLike라는 함수를 가져옴 파라미터에 postLikeDto를 넣음 이건 post가서 다시 봐 준철아.
            comment.deleteLike(commentLikeDto);
            return "삭제됨";
        }
        comment.addLike(commentLikeDto);
        commentLikeRepository.save(commentLike);
        return "무야호~";
    }
}

// post와 comment 분리해서 생각한다면 service에 있는 코드들 더 간결하게 줄일 수 있음.