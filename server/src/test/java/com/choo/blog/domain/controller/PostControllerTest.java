package com.choo.blog.domain.controller;

import com.choo.blog.domain.posts.PostOpenType;
import com.choo.blog.domain.posts.Post;
import com.choo.blog.domain.posts.dto.PostRequestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("게시물 관리")
class PostControllerTest {
    private static final String TITLE = "게시물 제목";
    private static final String CONTENT = "게시물 내용";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    @DisplayName("게시물 생성은")
    class Describe_create_post{
        @Nested
        @DisplayName("게시물을 입력받으면")
        class context_with_post{
            PostRequestData saveData;

            @BeforeEach
            public void setUp(){
                saveData = prepareRequestData("");
            }

            @Test
            @DisplayName("게시물을 생성하고 생성된 게시물을 반환한다.")
            public void it_return_new_posts() throws Exception{
                mockMvc.perform(post("/api/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaTypes.HAL_JSON)
                            .content(objectMapper.writeValueAsString(saveData)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(header().exists(HttpHeaders.LOCATION))
                        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                        .andExpect(jsonPath("title").value(saveData.getTitle()))
                        .andExpect(jsonPath("content").value(saveData.getContent()))
                        .andExpect(jsonPath("likes").value(0))
                        .andExpect(jsonPath("dislikes").value(0))
                        .andExpect(jsonPath("openType").value(saveData.getOpenType().toString()))
                        .andExpect(jsonPath("view").value(0))
                        .andExpect(jsonPath("_links.self").exists());

            }
        }

        @Nested
        @DisplayName("빈 데이터를 입력받으면")
        class context_with_empty_data{
            PostRequestData saveData;
            @BeforeEach
            public void setUp(){
                saveData = new PostRequestData();
            }

            @Test
            @DisplayName("에러코드 400를 반환한다.")
            public void it_return_bad_request() throws Exception{
                mockMvc.perform(post("/api/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)
                                .content(objectMapper.writeValueAsString(saveData)))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("errors[0].objectName").exists())
                        .andExpect(jsonPath("errors[0].code").exists())
                        .andExpect(jsonPath("errors[0].rejectedValue").hasJsonPath());
            }
        }
    }

    @Nested
    @DisplayName("게시물 수정은")
    class Descrive_update{
        @Nested
        @DisplayName("게시물 id와 수정 정보가 주어지면")
        class context_with_postId_and_update_info{
            PostRequestData updateData;
            Post post;

            @BeforeEach
            void setUp() throws Exception {
                updateData = prepareRequestData("_NEW");
                post = preparePost("");
            }

            @Test
            @DisplayName("게시물을 수정하고 수정된 게시물을 반환한다.")
            void it_return_updated_posts() throws Exception {
                mockMvc.perform(patch("/api/posts/{id}",post.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaTypes.HAL_JSON)
                            .content(objectMapper.writeValueAsString(updateData)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").value(post.getId()))
                        .andExpect(jsonPath("title").value(updateData.getTitle()))
                        .andExpect(jsonPath("content").value(updateData.getContent()))
                        .andExpect(jsonPath("likes").value(post.getLikes()))
                        .andExpect(jsonPath("dislikes").value(post.getDislikes()))
                        .andExpect(jsonPath("openType").value(updateData.getOpenType().toString()))
                        .andExpect(jsonPath("view").value(post.getView()))
                        .andExpect(jsonPath("_links.self").exists());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 게시물 id가 주어지면")
        class context_with_non_exist_postId{
            PostRequestData updateData;

            @BeforeEach
            public void setUp(){
                updateData = prepareRequestData("_NEW");
            }
            @Test
            @DisplayName("에러코드 404를 반환한다.")
            public void it_return_postNotFoundException() throws Exception {
                mockMvc.perform(patch("/api/posts/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("message").exists())
                        ;

            }
        }

        @Nested
        @DisplayName("빈 데이터를 입력받는다면")
        class Context_with_wrong_data{
            PostRequestData updateData;

            @BeforeEach
            public void setUp(){
                updateData = new PostRequestData();
            }

            @Test
            @DisplayName("에러코드 400을 반환한다")
            public void it_return_badRequest() throws Exception{
                mockMvc.perform(post("/api/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)
                                .content(objectMapper.writeValueAsString(updateData)))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("errors[0].objectName").exists())
                        .andExpect(jsonPath("errors[0].code").exists())
                        .andExpect(jsonPath("errors[0].rejectedValue").hasJsonPath());
            }
        }
    }

    @Nested
    @DisplayName("게시물 목록 조회는")
    class Descrive_get_posts{
        @Nested
        @DisplayName("게시물 조회 조건을 입력받으면")
        class Context_with_search_condition{
            int page = 0;
            int pageSize = 10;
            int size = 30;


            Pageable pageable;

            @BeforeEach
            public void setUp() throws Exception{
                pageable = PageRequest.of(page, pageSize);

                for(int i = 0; i < size; i++){
                    preparePost(i + "");
                }
            }

            @Test
            @DisplayName("조회 결과를 반환한다.")
            public void it_return_paging_posts() throws Exception {
                mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(pageable)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("page").exists())
                        .andExpect(jsonPath("_links.self").exists())
                        .andExpect(jsonPath("_embedded.postsList[0]._links.self").exists());
            }
        }
    }

    @Nested
    @DisplayName("게시물 조회는")
    class Describe_get_post{
        @Nested
        @DisplayName("존재하는 게시물 id가 주어지면")
        class Context_with_exist_postId{
            Post post;

            @BeforeEach
            public void setUp() throws Exception {
                post = preparePost("");
            }

            @Test
            @DisplayName("id에 해당하는 게시물을 반환한다.")
            void it_return_post() throws Exception {
                mockMvc.perform(get("/api/posts/{id}", post.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaTypes.HAL_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("_links.self").exists())
                        .andExpect(jsonPath("title").value(post.getTitle()))
                        .andExpect(jsonPath("content").value(post.getContent()))
                        .andExpect(jsonPath("likes").value(post.getLikes()))
                        .andExpect(jsonPath("dislikes").value(post.getDislikes()))
                        .andExpect(jsonPath("view").value(post.getView()));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 게시물 id가 주어지면")
        class Context_with_non_exist_postId{
            @Test
            @DisplayName("에러코드 404를 반환한다.")
            void it_return_notFound() throws Exception {
                mockMvc.perform(get("/api/posts/{id}", -1))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("message").value(Matchers.containsString("-1")));
            }
        }
    }

    @Nested
    @DisplayName("게시물 삭제는")
    class Descrive_delete_post{
        @Nested
        @DisplayName("존재하는 게시물 id가 주어지면")
        class Context_with_exist_postId{
            Post post;

            @BeforeEach
            void setUp() throws Exception {
                post = preparePost("");
            }

            @Test
            @DisplayName("게시물을 삭제하고 HTTP code 200을 반환한다")
            void it_return_ok() throws Exception {
                mockMvc.perform(delete("/api/posts/{id}", post.getId()))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 게시물 id가 주어지면")
        class Context_with_non_eixst_postId{

            @Test
            @DisplayName("에러코드 404를 반환한다.")
            void it_return_notFound() throws Exception{
                mockMvc.perform(delete("/api/posts/{id}", -1))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("message").value(Matchers.containsString("-1")));
            }
        }
    }

    private Post preparePost(String suffix) throws Exception{
        MvcResult result = mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(prepareRequestData(suffix)))).andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println(content);
        return objectMapper.readValue(content, Post.class);
    }


    private PostRequestData prepareRequestData(String suffix){
        return PostRequestData.builder()
                .title(TITLE + suffix)
                .content(CONTENT + suffix)
                .openType(PostOpenType.ALL)
                .build();
    }
}