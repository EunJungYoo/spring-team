package com.example.demo.domain;

import com.example.demo.domain.dto.PostRequestDto;
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
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "postId", nullable = false, unique = true)
    private Long postId;

    @Column(nullable = false, length = 31)
    private String title;

    @Column
    private Long likeCount = 0L;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId", updatable = false, nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();


    public Post(PostRequestDto postRequestDto, User user) {
        super();
        this.title = postRequestDto.getTitle();
        this.user = user;
        this.content = postRequestDto.getContent();
    }

    public void update(PostRequestDto postRequestDto){
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }





}
