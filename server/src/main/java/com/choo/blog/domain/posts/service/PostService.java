package com.choo.blog.domain.posts.service;

import com.choo.blog.domain.posts.repository.PostRepository;
import com.choo.blog.domain.posts.Post;
import com.choo.blog.domain.posts.dto.PostRequestData;
import com.choo.blog.exceptions.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public Page<Post> getPosts(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    public Post getPost(Long id){
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    public Post save(PostRequestData saveData){
        return postRepository.save(saveData.createEntity());
    }

    public Post update(Long id, PostRequestData updateData){
        Post posts = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        posts.update(updateData);

        return posts;
    }

    public void delete(Long id){
        Post post = getPost(id);
        postRepository.delete(post);
    }
}
