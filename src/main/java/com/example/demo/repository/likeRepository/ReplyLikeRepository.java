package com.example.demo.repository.likeRepository;

import com.example.demo.domain.LikeDomain.ReplyLike;
import com.example.demo.domain.Reply;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    Optional<ReplyLike> findByReplyAndUser(Reply reply, User user);
    void deleteByReplyAndUser(Reply reply, User user);
}
