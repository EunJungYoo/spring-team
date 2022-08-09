package com.example.demo.repository.likeRepository;

import com.example.demo.domain.LikeDomain.PostLike;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, User user);
    void deleteByPostAndUser(Post post, User user);
}
