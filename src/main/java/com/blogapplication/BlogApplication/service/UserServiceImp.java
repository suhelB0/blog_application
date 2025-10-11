package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.User;
import com.blogapplication.BlogApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAuthors() {
        return userRepository.findAll();
    }
}
