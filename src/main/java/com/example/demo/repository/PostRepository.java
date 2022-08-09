package com.example.demo.repository;

import com.example.demo.domain.Post;
import com.example.demo.repository.SelectJPAColumnInterface.ShowPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<ShowPost> findAllByOrderByCreatedAtDesc();
}
