package com.blogapplication.BlogApplication.repository;

import com.blogapplication.BlogApplication.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String tagName);

    @Query("SELECT DISTINCT t FROM Tag t JOIN t.posts p WHERE p.isPublished = true")
    List<Tag> findTagsOfPublishedPosts();
}
