package com.choo.blog.dto.posts;

import com.choo.blog.domain.posts.PostOpenType;
import com.choo.blog.domain.posts.Posts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestData {
    private String title;
    private String content;
    private PostOpenType openType;

    public Posts createEntity(){
        return Posts.builder()
                .title(title)
                .content(content)
                .likes(0)
                .dislikes(0)
                .view(0)
                .openType(openType)
                .build();
    }
}
