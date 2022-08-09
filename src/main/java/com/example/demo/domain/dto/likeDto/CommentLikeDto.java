package com.example.demo.domain.dto.likeDto;

import com.example.demo.domain.Comment;
import com.example.demo.domain.User;
import lombok.Data;

@Data
public class CommentLikeDto {
    private User user;
    private Comment comment;

    public CommentLikeDto() {

    }
    public CommentLikeDto(User user, Comment comment){
        this.user = user;
        this.comment = comment;
    }
}
