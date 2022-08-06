package com.example.demo.domain.dto.likeDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CommentLikeDto {
    private String user_id;
    private String comment_id;
}
