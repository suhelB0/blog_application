package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.User;

import java.util.List;

public interface UserService {
    User getUserById(int id);

    List<User> getAuthors();
}
