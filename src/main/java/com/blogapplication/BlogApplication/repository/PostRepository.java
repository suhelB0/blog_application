package com.blogapplication.BlogApplication.repository;

import com.blogapplication.BlogApplication.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN p.tags t " +
            "WHERE p.isPublished = true AND (" +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) )")
    List<Post> searchPosts(@Param("keyword") String keyword);

    List<Post> findAllByIsPublishedTrueOrderByPublishedAtAsc();

    List<Post> findAllByIsPublishedTrueOrderByPublishedAtDesc();

    @Query("SELECT p FROM Post p WHERE p.isPublished = true AND (" +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.excerpt) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.user.name) LIKE LOWER(CONCAT('%', :query, '%')) ) " +
            "ORDER BY p.publishedAt ASC")
    List<Post> searchPostsSortedOld(@Param("query") String query);

    @Query("SELECT p FROM Post p WHERE p.isPublished = true AND (" +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.excerpt) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.user.name) LIKE LOWER(CONCAT('%', :query, '%')) ) " +
            "ORDER BY p.publishedAt DESC")
    List<Post> searchPostsSortedNew(@Param("query") String query);

}

