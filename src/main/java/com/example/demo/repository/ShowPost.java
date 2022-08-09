package com.example.demo.repository;

import java.time.LocalDateTime;

public interface ShowPost {

    Long getPostId();
    String getTitle();
    String getContent();
    UserSummaryInfo getUser();
    Long getLikeCount();

    String getImageUrl();
    LocalDateTime getCreatedAt();
    LocalDateTime getModifiedAt();

    interface UserSummaryInfo {
        String getUserId();
        String getUsername();
    }

}
