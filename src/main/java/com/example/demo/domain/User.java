package com.example.demo.domain;

import com.example.demo.domain.LikeDomain.CommentLike;
import com.example.demo.domain.LikeDomain.PostLike;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @Column(name = "userId", nullable = false, unique = true, length = 20)
    private String userId;

    @Column(nullable = false, length = 15)
    private String username;

    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy="user")
    private Set<Comment> commentList;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy="user")
    private Set<Post> postList;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy="user")
    private Set<CommentLike> commentLikeList;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy="user")
    private Set<PostLike> postLikeList;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "userId"))
    private Set<Authority> authorities;


    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

}