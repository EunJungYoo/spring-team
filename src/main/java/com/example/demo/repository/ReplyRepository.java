package com.example.demo.repository;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//CRUD기능을 위해 ReplyRepository 생성
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply>findAllByComment(Comment comment);//Comment를 find(get)시 Reply를 보여주기 위해 작성? 문제생기면 띄어쓰기
}

