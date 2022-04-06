package com.choo.blog.domain.posts;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import com.choo.blog.controller.PostController;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class PostModel extends EntityModel<Posts> {

    public PostModel(Posts posts, Link... links) {
        super(posts, (Iterable) Arrays.asList(links));
        add(linkTo(PostController.class).slash(posts.getId()).withSelfRel());

    }
}
