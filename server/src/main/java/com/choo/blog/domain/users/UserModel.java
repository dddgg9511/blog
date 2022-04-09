package com.choo.blog.domain.users;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class UserModel extends EntityModel<User> {
    public UserModel(User user, Link... links) {
        super(user, (Iterable) Arrays.asList(links));
        add(linkTo(UserController.class).slash(user.getId()).withSelfRel());
    }
}
