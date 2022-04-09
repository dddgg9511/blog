package com.choo.blog.service.posts;

import com.choo.blog.domain.posts.PostRepository;
import com.choo.blog.domain.posts.Post;
import com.choo.blog.dto.posts.PostRequestData;
import com.choo.blog.exceptions.PostNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.stream.IntStream;

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
                Post posts = postService.save(saveData);

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
            Post posts;

            @BeforeEach
            public void setUp(){
                posts = postService.save(prepareRequestData(""));
            }

            @Test
            @DisplayName("id에 해당하는 게시물을 수정하고 수정된 게시물을 반환한다.")
            void it_update_post_return(){
                Post updatePost = postService.update(posts.getId(), updateData);

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

    @Nested
    @DisplayName("게시물 목록 조회는")
    class Descrive_findAll{
        @Nested
        @DisplayName("조회조건을 입력받으면")
        class Context_with_search_condition{
            int page = 0;
            int pageSize = 10;
            int size = 30;

            Pageable pageable;

            @BeforeEach
            public void setUp(){
                pageable = PageRequest.of(page,pageSize);

                IntStream.range(0, size).forEach(i ->{
                    postRepository.save(prepareRequestData(i + "").createEntity());
                });
            }

            @Test
            @DisplayName("조회 결과를 반환한다.")
            public void it_return_paging_posts(){
                Page<Post> posts = postService.getPosts(pageable);
                assertThat(posts.getTotalElements()).isEqualTo(size);
                assertThat(posts.getTotalPages()).isEqualTo(size / pageSize);
                assertThat(posts.getNumberOfElements()).isEqualTo(pageSize);
            }
        }
    }

    @Nested
    @DisplayName("게시물 삭제는")
    class Descrive_delete{
        @Nested
        @DisplayName("등록된 게시물 id가 주어진다면")
        class context_with_exist_postId{
            Post posts;
            @BeforeEach
            public void setUp(){
                posts = postRepository.save(prepareRequestData("").createEntity());
            }
            @Test
            @DisplayName("게시물을 삭제한다.")
            void it_delete_post(){
                postService.delete(posts.getId());

                Optional<Post> optionalPosts = postRepository.findById(posts.getId());
                assertThat(optionalPosts).isEmpty();
            }
        }

        @Nested
        @DisplayName("등록되지 않은 게시물 id가 주어진다면")
        class Context_with_non_exise_postId{
            @Test
            @DisplayName("게시물을 찾을 수 없다는 예외를 던진다")
            public void it_throw_postNotFoundException(){
                assertThatThrownBy(()-> postService.delete(-1L))
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