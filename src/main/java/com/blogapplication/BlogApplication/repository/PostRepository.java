package com.blogapplication.BlogApplication.repository;

import com.blogapplication.BlogApplication.Entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByIsPublishedTrue();

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN p.tags t " +
            "WHERE p.isPublished = true AND (" +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) )")
    List<Post> searchPostsSort(@Param("keyword") String keyword, Sort sort);

    List<Post> findAllByIsPublishedTrueOrderByPublishedAtAsc();

    List<Post> findAllByIsPublishedTrueOrderByPublishedAtDesc();

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE p.isPublished = true " +
            "AND (:tags IS NULL OR t.name IN :tags) " +
            "AND (:authors IS NULL OR p.user.name IN :authors)")
    List<Post> filterPostsByTagsAndAuthorsSort(@Param("tags") String[] tags, @Param("authors") String[] authors, Sort sort);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN p.tags t " +
            "WHERE p.isPublished = true " +
            "AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.user.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:tags IS NULL OR t.name IN :tags) " +
            "AND (:authors IS NULL OR p.user.name IN :authors)")
    List<Post> searchPostsWithFiltersSort(@Param("search") String search, @Param("tags") String[] tags, @Param("authors") String[] authors, Sort sort);
}

