package com.example.demo.repository;

import com.example.demo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<ShowPost> findByPostIdOrderByCreatedAtDesc(Long id);
    List<ShowPost> findAllByOrderByCreatedAtDesc();
}
