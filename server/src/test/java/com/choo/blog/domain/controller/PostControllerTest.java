package com.choo.blog.domain.controller;

import com.choo.blog.domain.posts.PostOpenType;
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
                mockMvc.perform(post("/posts")
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
                        .andExpect(jsonPath("openType").value(saveData.getOpenType()))
                        .andExpect(jsonPath("view").value(0))
                        .andExpect(jsonPath("_links.self").exists());

            }
        }
    }


    private PostRequestData prepareRequestData(String suffix){
        return PostRequestData.builder()
                .title(TITLE + suffix)
                .content(CONTENT + suffix)
                .openType(PostOpenType.ALL)
                .build();
    }
}