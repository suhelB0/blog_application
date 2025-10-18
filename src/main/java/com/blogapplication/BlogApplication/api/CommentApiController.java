package com.blogapplication.BlogApplication.api;

import com.blogapplication.BlogApplication.Entity.Comment;
import com.blogapplication.BlogApplication.Entity.Post;
import com.blogapplication.BlogApplication.service.CommentService;
import com.blogapplication.BlogApplication.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog")
public class CommentApiController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> readComment(@PathVariable("id") int id){
        Comment comment = commentService.getCommentById(id);
        if (comment == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/comments/{id}")
    public ResponseEntity<String> saveComment(@RequestBody Comment comment, @PathVariable("id") int id){
        Post post = postService.getPostById(id);
        comment.setPost(post);
        commentService.saveComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body("Comment created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") int id){
        commentService.deleteCommentById(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateComment(@RequestBody Comment comment, @PathVariable("id") int id){
        Comment existingComment = commentService.getCommentById(id);
        existingComment.setName(comment.getName());
        existingComment.setEmail(comment.getEmail());
        existingComment.setCommentText(comment.getCommentText());
        commentService.saveComment(existingComment);
        return ResponseEntity.ok("Comment updated successfully");
    }
}
