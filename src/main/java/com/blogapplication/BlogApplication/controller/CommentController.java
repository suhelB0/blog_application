package com.blogapplication.BlogApplication.controller;

import com.blogapplication.BlogApplication.Entity.Comment;
import com.blogapplication.BlogApplication.Entity.Post;
import com.blogapplication.BlogApplication.service.CommentService;
import com.blogapplication.BlogApplication.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @PostMapping("/saveComment/{id}")
    public String saveComment(@ModelAttribute Comment comment, @PathVariable("id") int id, Model model){
        comment.setId(0);
        Post post = postService.getPostById(id);
        comment.setPost(post);
        commentService.saveComment(comment);

        return "redirect:/readPost/"+id;
    }

    @GetMapping("/deleteComment/{id}")
    public String deleteComment(@PathVariable("id") int id){
        Comment comment = commentService.getCommentById(id);
        int postId = comment.getPost().getId();
        commentService.deleteCommentById(id);
        return "redirect:/readPost/"+postId;
    }

    @GetMapping("/editComment/{id}")
    public String editComment(@PathVariable("id") int id, Model model){
        Comment comment = commentService.getCommentById(id);
        model.addAttribute("comment",comment);
        return "editComment";
    }

    @PostMapping("/updateComment/{id}")
    public String updateComment(@ModelAttribute Comment comment, @PathVariable("id") int id){
        Comment existingComment = commentService.getCommentById(id);
        existingComment.setName(comment.getName());
        existingComment.setEmail(comment.getEmail());
        existingComment.setCommentText(comment.getCommentText());
        commentService.saveComment(existingComment);

        return "redirect:/readPost/"+existingComment.getPost().getId();
    }
}
