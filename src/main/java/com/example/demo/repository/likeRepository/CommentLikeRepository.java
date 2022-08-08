package com.example.demo.repository.likeRepository;

import com.example.demo.domain.LikeDomain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

}
