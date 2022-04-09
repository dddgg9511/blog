package com.choo.blog.dto.posts;

import com.choo.blog.domain.posts.PostOpenType;
import com.choo.blog.domain.posts.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestData {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;

    private PostOpenType openType;

    public Post createEntity(){
        return Post.builder()
                .title(title)
                .content(content)
                .likes(0)
                .dislikes(0)
                .view(0)
                .openType(openType)
                .build();
    }
}
