package com.choo.blog.domain.users;

import com.choo.blog.domain.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Users extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String pw;
    private String nickname;
    private String image;
    private LocalDateTime birthdate;
    private String description;
}
