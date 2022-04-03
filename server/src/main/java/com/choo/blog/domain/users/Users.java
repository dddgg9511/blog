package com.choo.blog.domain.users;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Users {
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
