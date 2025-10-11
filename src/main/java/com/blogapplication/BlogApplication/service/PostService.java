package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.Post;

import java.util.List;

public interface PostService {
    public Post getPostById(int id);

    public List<Post> getAllPost();

    public void saveOrUpdatePost(Post post, String tagString);

    void deletePostById(int id);

    List<Post> searchPosts(String keyword);

    List<Post> getAllPostsSorted(String sortBy);

    List<Post> getAllPostsSortedOld();

    List<Post> getAllPostsSortedNew();

    List<Post> searchPostsSortedOld(String search);

    List<Post> searchPostsSortedNew(String search);
}
