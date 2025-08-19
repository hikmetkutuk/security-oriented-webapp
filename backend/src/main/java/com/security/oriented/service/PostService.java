package com.security.oriented.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.security.oriented.dto.PostRequest;
import com.security.oriented.dto.PostResponse;
import com.security.oriented.mapper.PostMapper;
import com.security.oriented.model.Post;
import com.security.oriented.repository.PostRepository;

@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse createPost(PostRequest req, String username) {
        try {
            Post post = PostMapper.toPost(req);
            post.setAuthor(username);
            Post newPost = postRepository.save(post);
            logger.info("Post created successfully with ID: {}", newPost.getId());
            return PostMapper.fromPost(newPost);
        } catch (Exception e) {
            logger.error("Error creating post: {}", e.getMessage());
            throw new RuntimeException("Failed to create post", e);
        }
    }

    public List<PostResponse> getAllPosts() {
        try {
            List<Post> posts = postRepository.findAll();
            logger.info("Retrieved {} posts", posts.size());
            return posts.stream()
                    .map(PostMapper::fromPost)
                    .toList();

        } catch (Exception e) {
            logger.error("Error retrieving posts: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve posts", e);
        }
    }
}
