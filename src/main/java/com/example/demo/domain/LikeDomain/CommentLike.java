package com.example.demo.domain.LikeDomain;

import com.example.demo.domain.Comment;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.likeDto.CommentLikeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "comment_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;


    public CommentLike(CommentLikeDto commentLikeDto) {
        this.user = commentLikeDto.getUser();
        this.comment = commentLikeDto.getComment();
    }
}
