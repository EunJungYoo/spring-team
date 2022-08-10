package com.example.demo.domain;

import com.example.demo.domain.LikeDomain.CommentLike;
import com.example.demo.domain.LikeDomain.ReplyLike;
import com.example.demo.domain.dto.CommentRequestDto;
import com.example.demo.domain.dto.likeDto.CommentLikeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId", nullable = false, unique = true)
    private Long commentId;

    @Column
    private Long likeCount = 0L;

    @ManyToOne
    @JoinColumn(name = "postId",updatable = false)
    @JsonIgnore
    private Post post;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId",updatable = false)
    private User user;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy ="comment")
    private Set<CommentLike> commentLikeList;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy ="comment")
    private Set<Reply> replyList;


    public Comment(String content, Post post, User user) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.post.setCommentCount(this.post.getCommentCount() + 1);
    }

    public boolean update(CommentRequestDto commentRequestDto){
        this.content = commentRequestDto.getContent();
        return true;
    }

    public void addLike(CommentLikeDto commentLikeDto) {
        this.user = commentLikeDto.getUser();
        this.commentId = commentLikeDto.getComment().getCommentId();
        likeCount++;
    }

    public void deleteLike(CommentLikeDto commentLikeDto) {
        this.user = commentLikeDto.getUser();
        this.commentId = commentLikeDto.getComment().getCommentId();
        likeCount--;
    }
}