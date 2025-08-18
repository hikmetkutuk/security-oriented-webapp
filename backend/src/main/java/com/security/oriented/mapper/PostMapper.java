package com.security.oriented.mapper;

import com.security.oriented.dto.PostRequest;
import com.security.oriented.dto.PostResponse;
import com.security.oriented.model.Post;

public class PostMapper {
    public static Post toPost(PostRequest request) {
        Post post = new Post();
        post.setTitle(request.title());
        post.setContent(request.content());
        post.setAuthor(request.author());
        return post;
    }

    public static PostResponse fromPost(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor());
    }
}
