package com.example.demo.domain;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="authority")
@IdClass(Authority.class)
public class Authority implements GrantedAuthority {

    @Id
    @Column(name="userId")
    private String userId;

    @Id
    private String authority;
}