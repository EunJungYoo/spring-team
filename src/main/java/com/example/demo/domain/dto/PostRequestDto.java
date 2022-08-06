package com.example.demo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {
    private String content;
    private String title;
    private String imageUrl;

}
