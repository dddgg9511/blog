package com.choo.blog.domain.posts;

import com.choo.blog.domain.BaseEntiry;
import com.choo.blog.domain.categories.Category;
import com.choo.blog.domain.comments.Comments;
import com.choo.blog.domain.users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posts extends BaseEntiry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users author;

    private String title;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private int like;

    private int dislike;

    @Enumerated
    private PostOpenType openType;

    private int view;

    @OneToMany(mappedBy = "posts")
    private List<Comments> commentsList = new ArrayList<>();
}
