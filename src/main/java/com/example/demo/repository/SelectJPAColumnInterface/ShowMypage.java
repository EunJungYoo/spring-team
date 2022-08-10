package com.example.demo.repository.SelectJPAColumnInterface;

import com.example.demo.domain.LikeDomain.CommentLike;
import com.example.demo.domain.LikeDomain.PostLike;
import com.example.demo.domain.LikeDomain.ReplyLike;
import com.example.demo.domain.Reply;

import java.time.LocalDateTime;
import java.util.Set;

public interface ShowMypage {

    Set<CommentSummaryInfo> getCommentList();
    Set<PostSummaryInfo> getPostList();
    Set<Reply> getReplyList();
    Set<CommentLikeSummaryInfo> getCommentLikeList();
    Set<PostLikeSummaryInfo> getPostLikeList();
    Set<ReplyLike> getReplyLikeList();

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
        String getTitle();
        LocalDateTime getCreatedAt();
        LocalDateTime getModifiedAt();
    }

    interface PostLikeSummaryInfo {
        Long getId();
        Set<PostSummaryInfo> getPost();
    }
    interface CommentLikeSummaryInfo {
        Long getId();
        Set<CommentSummaryInfo> getComment();
    }



}
