package com.example.demo.domain.imageDomain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AwsS3 {
    private String key;
    private String path;

    @Builder
    public AwsS3(String key, String path) {
        this.key = key;
        this.path = path;
    }
}
