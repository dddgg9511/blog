package com.choo.blog.dto.posts;

import com.choo.blog.domain.posts.PostOpenType;
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
}
