package com.example.demo.config.jwt;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class VerifyResult {

    private boolean success;
    private String userId;


}
