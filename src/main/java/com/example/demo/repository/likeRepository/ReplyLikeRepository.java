package com.example.demo.repository.likeRepository;

import com.example.demo.domain.LikeDomain.ReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    List<ReplyLike> findByReplyLike(Long Id);
}
