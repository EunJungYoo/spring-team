package com.example.demo.domain.dto.likeDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostLikeDto {
    private String user_id;
    private Long post_id;
}
