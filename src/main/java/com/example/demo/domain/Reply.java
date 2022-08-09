package com.example.demo.domain;

import com.example.demo.domain.dto.ReplyRequestDto;
import com.example.demo.domain.dto.likeDto.ReplyLikeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//대댓글 수정등의 기능 사용을 위해 Setter추가
@Setter
public class Reply extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @JsonIgnore//무한 참조로 인하여 추가
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private User user;

    @JoinColumn(name = "comment_id", nullable = false)
    @ManyToOne
    @JsonIgnore
    private Comment comment;

    @Column
    private Long likeCount = 0L;

    @Column
    private String content;
    //대댓글 작성관련으로 인하여 생성자 생성
    public Reply(String content, Comment comment, User user) {
        this.content = content;
        this.comment = comment;
        this.user = user;

    }
    //대댓글 수정관련으로 내용추가
    public boolean update(ReplyRequestDto replyRequestDto){
        this.content = replyRequestDto.getContent();
        return true;
    }

    public void addLike(ReplyLikeDto replyLikeDto) {
        this.user = replyLikeDto.getUser();
        this.replyId = replyLikeDto.getReply().getReplyId();
        likeCount++;
    }

    public void deleteLike(ReplyLikeDto replyLikeDto) {
        this.user = replyLikeDto.getUser();
        this.replyId = replyLikeDto.getReply().getReplyId();
        likeCount--;
    }

}
