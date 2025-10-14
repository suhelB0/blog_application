package com.blogapplication.BlogApplication.repository;

import com.blogapplication.BlogApplication.Entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByIsPublishedTrue(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN p.tags t " +
            "WHERE p.isPublished = true AND (" +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) )")
    Page<Post> searchPostsSort(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE p.isPublished = true " +
            "AND (:tags IS NULL OR t.name IN :tags) " +
            "AND (:authors IS NULL OR p.user.name IN :authors)")
    Page<Post> filterPostsByTagsAndAuthorsSort(@Param("tags") String[] tags, @Param("authors") String[] authors, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN p.tags t " +
            "WHERE p.isPublished = true " +
            "AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.user.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:tags IS NULL OR t.name IN :tags) " +
            "AND (:authors IS NULL OR p.user.name IN :authors)")
    Page<Post> searchPostsWithFiltersSort(@Param("search") String search, @Param("tags") String[] tags, @Param("authors") String[] authors, Pageable pageable);
}

