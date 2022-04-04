package com.choo.blog.domain.comments;

import com.choo.blog.domain.BaseEntity;
import com.choo.blog.domain.posts.Posts;

import javax.persistence.*;

@Entity
public class Comments extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts posts;

    private String content;

    private String author;

    private boolean secret;

    private int likes;

    private int dislikes;
}
