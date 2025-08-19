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

    public PostResponse updatePost(Long id, PostRequest req, String username) {
        try {
            Post existingPost = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
            if (!existingPost.getAuthor().equals(username)) {
                throw new RuntimeException("You are not authorized to update this post");
            }
            Post updatedPost = PostMapper.toPost(req);
            updatedPost.setAuthor(username);
            Post savedPost = postRepository.save(updatedPost);
            logger.info("Post updated successfully with ID: {}", savedPost.getId());
            return PostMapper.fromPost(savedPost);
        } catch (Exception e) {
            logger.error("Error updating post: {}", e.getMessage());
            throw new RuntimeException("Failed to update post", e);
        }
    }

    public void deletePost(Long id, String username) {
        try {
            Post existingPost = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
            if (!existingPost.getAuthor().equals(username)) {
                throw new RuntimeException("You are not authorized to delete this post");
            }
            postRepository.delete(existingPost);
            logger.info("Post deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting post: {}", e.getMessage());
            throw new RuntimeException("Failed to delete post", e);
        }
    }
}
