package com.choo.blog.domain.comments;

import com.choo.blog.domain.posts.Posts;

import javax.persistence.*;

@Entity
public class Comments {
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts posts;

    private String content;

    private String author;

    private boolean secret;

    private int like;

    private int dislike;
}
