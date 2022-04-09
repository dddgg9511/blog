package com.choo.blog.domain.controller;

import com.choo.blog.domain.posts.PostOpenType;
import com.choo.blog.domain.posts.Posts;
import com.choo.blog.dto.posts.PostRequestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
            Posts post;

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

    private Posts preparePost(String suffix) throws Exception{
        MvcResult result = mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(prepareRequestData(suffix)))).andReturn();
        String content = result.getResponse().getContentAsString();
        return objectMapper.readValue(content, Posts.class);
    }


    private PostRequestData prepareRequestData(String suffix){
        return PostRequestData.builder()
                .title(TITLE + suffix)
                .content(CONTENT + suffix)
                .openType(PostOpenType.ALL)
                .build();
    }
}