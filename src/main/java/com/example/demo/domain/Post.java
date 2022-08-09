package com.example.demo.domain;

import com.example.demo.domain.LikeDomain.PostLike;
import com.example.demo.domain.dto.PostRequestDto;
import com.example.demo.domain.dto.likeDto.PostLikeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    //필수적으로 넣어야하는 필드는 아니므로, nullable = true
    @Column(nullable = true)
    private String imageUrl;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Comment> commentList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy ="post")
    private Set<PostLike> postLikeList;

    public Post(PostRequestDto postRequestDto, User user) {
        this.title = postRequestDto.getTitle();
        this.user = user;
        this.content = postRequestDto.getContent();
        this.imageUrl = postRequestDto.getImageUrl();
    }

    public void update(PostRequestDto postRequestDto){
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }


    public void addLike(PostLikeDto postLikeDto) {
        this.user = postLikeDto.getUser();
        this.postId = postLikeDto.getPost().getPostId();
        likeCount++;
    }

    public void deleteLike(PostLikeDto postLikeDto) {
        this.user = postLikeDto.getUser();
        this.postId = postLikeDto.getPost().getPostId();
        likeCount--;
    }
}
