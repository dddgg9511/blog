package com.choo.blog.service.posts;

import com.choo.blog.domain.posts.PostOpenType;
import com.choo.blog.domain.posts.PostRepository;
import com.choo.blog.domain.posts.Posts;
import com.choo.blog.dto.posts.PostRequestData;
import com.choo.blog.exceptions.PostNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("게시물 관리")
class PostServiceTest {
    private static final String TITLE = "게시물 제목";
    private static final String CONTENT = "게시물 내용";

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Nested
    @DisplayName("게시물 저장은")
    class Descrive_save{

        @Nested
        @DisplayName("게시물을 입력받으면")
        class Context_with_post{
            PostRequestData saveData;

            @BeforeEach
            void setUp(){
                saveData = prepareRequestData("");
            }

            @Test
            @DisplayName("저장하고 저장된 게시물을 반환한다.")
            void it_return_post(){
                Posts posts = postService.save(saveData);

                assertThat(posts.getTitle()).isEqualTo(saveData.getTitle());
                assertThat(posts.getContent()).isEqualTo(saveData.getContent());
                assertThat(posts.getOpenType()).isEqualTo(saveData.getOpenType());
                assertThat(posts.getLikes()).isEqualTo(0);
                assertThat(posts.getDislikes()).isEqualTo(0);
                assertThat(posts.getView()).isEqualTo(0);
            }
        }
    }

    @Nested
    @DisplayName("게시물 수정은")
    class Descrive_update{
        PostRequestData updateData;

        @BeforeEach
        public void setUp(){
            updateData = prepareRequestData("_NEW");
        }

        @Nested
        @DisplayName("등록된 게시물 id가 주어진다면")
        class Context_with_exist_postId{
            Posts posts;

            @BeforeEach
            public void setUp(){
                posts = postService.save(prepareRequestData(""));
            }

            @Test
            @DisplayName("id에 해당하는 게시물을 수정하고 수정된 게시물을 반환한다.")
            void it_update_post_return(){
                Posts updatePost = postService.update(posts.getId(), updateData);

                assertThat(updatePost.getTitle()).isEqualTo(updateData.getTitle());
                assertThat(updatePost.getContent()).isEqualTo(updateData.getContent());
                assertThat(updatePost.getOpenType()).isEqualTo(updateData.getOpenType());
            }
        }

        @Nested
        @DisplayName("등록되지 않은 게시물 id가 주어진다면")
        class Context_with_not_exist_postId{
            @Test
            @DisplayName("게시물을 찾을 수 없다는 예외를 던진다")
            public void it_throw_postNotFoundException(){
                assertThatThrownBy(() -> postService.update(1111L, updateData))
                        .isInstanceOf(PostNotFoundException.class);
            }
        }
    }

    private PostRequestData prepareRequestData(String suffix){
        return PostRequestData.builder()
                .title(TITLE + suffix)
                .content(CONTENT + suffix)
                .build();
    }
}