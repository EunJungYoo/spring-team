package com.example.demo.repository;

import com.example.demo.domain.Comment;
import com.example.demo.domain.LikeDomain.CommentLike;
import com.example.demo.domain.LikeDomain.PostLike;
import com.example.demo.domain.Post;
import com.example.demo.domain.Reply;

import java.time.LocalDateTime;
import java.util.Set;

public interface ShowMypage {

    Set<CommentSummaryInfo> getCommentList();
    Set<PostSummaryInfo> getPostList();
    Set<Reply> getReplyList();
    Set<CommentLike> getCommentLikeList();
    Set<PostLike> getPostLikeList();

    interface CommentSummaryInfo {
        Long getCommentId();
        Long getLikeCount();
        String getContent();
        LocalDateTime getCreatedAt();
        LocalDateTime getModifiedAt();
    }

    interface PostSummaryInfo {
        Long getPostId();
        Long getLikeCount();
        String getContent();
        LocalDateTime getCreatedAt();
        LocalDateTime getModifiedAt();
    }

}
