package com.blogapplication.BlogApplication.repository;

import com.blogapplication.BlogApplication.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
