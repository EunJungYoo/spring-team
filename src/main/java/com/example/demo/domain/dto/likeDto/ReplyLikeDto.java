package com.example.demo.domain.dto.likeDto;

import com.example.demo.domain.Reply;
import com.example.demo.domain.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReplyLikeDto {
    private User user;
    private Reply reply;

    public ReplyLikeDto(User user, Reply reply) {
        this.user = user;
        this.reply = reply;
    }
}
