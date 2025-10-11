package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.Comment;

public interface CommentService {
    void saveComment(Comment comment);

    Comment getCommentById(int id);

    void deleteCommentById(int id);
}
