package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    Post getPostById(int id);

    void saveOrUpdatePost(Post post, String tagString);

    void deletePostById(int id);

    Page<Post> getFilteredPosts(String search, String sortField, String order, String[] tags, String[] authors, int start, int limit);
}
