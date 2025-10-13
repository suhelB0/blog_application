package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.Post;

import java.util.List;

public interface PostService {
    Post getPostById(int id);

    List<Post> getAllPost();

    void saveOrUpdatePost(Post post, String tagString);

    void deletePostById(int id);

    List<Post> getFilteredPosts(String search, String sortField, String order, String[] tags, String[] authors);
}
