package com.choo.blog.service.posts;

import com.choo.blog.domain.posts.PostRepository;
import com.choo.blog.domain.posts.Posts;
import com.choo.blog.dto.posts.PostRequestData;
import com.choo.blog.exceptions.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public Page<Posts> getPosts(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    public Posts getPost(Long id){
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    public Posts save(PostRequestData saveData){
        return postRepository.save(saveData.createEntity());
    }

    public Posts update(Long id, PostRequestData updateData){
        Posts posts = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        posts.update(updateData);

        return posts;
    }

    public void delete(Long id){
        Posts post = getPost(id);
        postRepository.delete(post);
    }
}
