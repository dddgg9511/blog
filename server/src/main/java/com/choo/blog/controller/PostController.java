package com.choo.blog.controller;

import com.choo.blog.domain.posts.PostModel;
import com.choo.blog.domain.posts.Posts;
import com.choo.blog.dto.posts.PostRequestData;
import com.choo.blog.exceptions.InvalidParameterException;
import com.choo.blog.service.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody @Valid PostRequestData saveData, BindingResult result){
        if(result.hasErrors()){
            throw new InvalidParameterException(result);
        }

        Posts posts = postService.save(saveData);
        PostModel postModel = new PostModel(posts);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(posts.getId());
        URI createdUri = selfLinkBuilder.toUri();

        return ResponseEntity.created(createdUri).body(postModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity updatePost(@PathVariable Long id,
                                     @RequestBody @Valid PostRequestData updateData,
                                     BindingResult result){
        if(result.hasErrors()){
            throw new InvalidParameterException(result);
        }

        Posts post = postService.update(id, updateData);
        PostModel postModel = new PostModel(post);

        return ResponseEntity.ok(postModel);
    }
}
