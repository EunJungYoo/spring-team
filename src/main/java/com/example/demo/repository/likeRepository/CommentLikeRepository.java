package com.example.demo.repository.likeRepository;

import com.example.demo.domain.Comment;
import com.example.demo.domain.LikeDomain.CommentLike;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);
    void deleteByCommentAndUser(Comment comment, User user);
}
