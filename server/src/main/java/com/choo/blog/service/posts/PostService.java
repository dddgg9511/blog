package com.choo.blog.service.posts;

import com.choo.blog.domain.posts.PostRepository;
import com.choo.blog.domain.posts.Posts;
import com.choo.blog.dto.posts.PostRequestData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public List<Posts> getPosts(){
        return null;
    }

    public Posts getPost(Long id){
        return null;
    }

    public Posts save(PostRequestData saveData){
        return null;
    }

    public Posts update(Long id, PostRequestData updateData){
        return null;
    }

    public Posts delete(Long id){
        return null;
    }
}
