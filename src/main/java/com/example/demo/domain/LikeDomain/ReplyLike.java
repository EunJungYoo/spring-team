package com.example.demo.domain.LikeDomain;

import com.example.demo.domain.Reply;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.likeDto.ReplyLikeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReplyLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

//////////////////////여기 마이페이지 출력 때문에 살림
    @JoinColumn(name = "reply_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Reply reply;

    public ReplyLike(ReplyLikeDto replyLikeDto) {
        this.user = replyLikeDto.getUser();
        this.reply = replyLikeDto.getReply();
    }
}
