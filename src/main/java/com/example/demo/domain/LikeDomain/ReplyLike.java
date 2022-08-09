package com.example.demo.domain.LikeDomain;

import com.example.demo.domain.Reply;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.likeDto.ReplyLikeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReplyLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "reply_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Reply reply;


    public ReplyLike(ReplyLikeDto replyLikeDto) {
        this.user = replyLikeDto.getUser();
        this.reply = replyLikeDto.getReply();
    }
}
