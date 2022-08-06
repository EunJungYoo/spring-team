package com.example.demo.domain;

import com.example.demo.domain.dto.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "commentId", nullable = false, unique = true)
    private Long commentId;

    @Column
    private Long likeCount = 0L;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "postId",updatable = false)
    private Post post;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId",updatable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy ="comment")
    private List<Reply> replyList = new ArrayList<>();




    public Comment(String content, Post post, User user) {
        super();
        this.content = content;
        this.user = user;
        this.post = post;
    }

    public boolean update(CommentRequestDto commentRequestDto){
        this.content = commentRequestDto.getContent();
        return true;
    }
}