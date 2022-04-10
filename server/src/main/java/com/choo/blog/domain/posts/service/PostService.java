package com.choo.blog.domain.posts.service;

import com.choo.blog.domain.posts.Post;
import com.choo.blog.domain.posts.dto.PostRequestData;
import com.choo.blog.domain.posts.repository.PostRepository;
import com.choo.blog.domain.users.User;
import com.choo.blog.domain.users.repository.UserRepository;
import com.choo.blog.exceptions.ForbiddenPostException;
import com.choo.blog.exceptions.PostNotFoundException;
import com.choo.blog.exceptions.UserNotFoundException;
import com.choo.blog.security.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    private final UserRepository userRepository;

    public Page<Post> getPosts(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    public Post getPost(Long id){
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    public Post save(PostRequestData saveData){
        User author = getLoginUser();

        return postRepository.save(saveData.createEntity(author));
    }

    public Post update(Long id, PostRequestData updateData){
        Post posts = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        User user = getLoginUser();

        if(!posts.getAuthor().getId().equals(user.getId())){
            throw new ForbiddenPostException(posts.getId());
        }
        posts.update(updateData);

        return posts;
    }

    public void delete(Long id){
        Post post = getPost(id);User user = getLoginUser();

        if(!post.getAuthor().getId().equals(user.getId())){
            throw new ForbiddenPostException(post.getId());
        }

        postRepository.delete(post);
    }

    private User getLoginUser(){
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findById(authentication.getUserId())
                .orElseThrow(() -> new UserNotFoundException(authentication.getUserId()));
    }
}
