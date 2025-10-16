package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.User;

import java.util.List;

public interface UserService {
    User getUserById(int id);

    User getUserByEmail(String email);

    List<User> getAuthors();

    void saveUser(User user);
}
