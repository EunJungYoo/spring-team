package com.example.demo.domain.dto.likeDto;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import lombok.Data;



@Data
public class PostLikeDto {
    private User user;
    private Post post;

    public PostLikeDto() {
    }

    public PostLikeDto(User user, Post post) {
        this.user = user;
        this.post = post;
    }


}
