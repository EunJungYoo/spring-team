package com.example.demo.domain.dto.likeDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReplyLikeDto {
    private String user_id;
    private Long Reply_id;
}
