package com.blogapplication.BlogApplication.controller;

import com.blogapplication.BlogApplication.Entity.Comment;
import com.blogapplication.BlogApplication.Entity.Post;
import com.blogapplication.BlogApplication.Entity.Tag;
import com.blogapplication.BlogApplication.service.PostService;
import com.blogapplication.BlogApplication.service.TagService;
import com.blogapplication.BlogApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @GetMapping("/newPost")
    public String addPost(Model model){
        model.addAttribute("post", new Post());
        return "addPost";
    }

    @PostMapping("/savePost")
    public String savePost(@ModelAttribute Post post, @RequestParam("tag") String tagString){

        postService.saveOrUpdatePost(post, tagString);
        return "redirect:/home";
    }

    @GetMapping("/")
    public String homePage(){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(@RequestParam(value = "search", defaultValue = "", required = false) String search,
                       @RequestParam(value = "sortBy", defaultValue = "0", required = false) int sortBy, Model model){
        List<Post> posts;
        if(!search.isEmpty() && sortBy==0){
            posts = postService.searchPosts(search);
        }
        else if(search.isEmpty() && sortBy==1){
            posts = postService.getAllPostsSortedOld();
        }
        else if(search.isEmpty() && sortBy==2){
            posts = postService.getAllPostsSortedNew();
        }
        else if(!search.isEmpty() && sortBy==1){
            posts = postService.searchPostsSortedOld(search);
        }
        else if(!search.isEmpty() && sortBy==2) {
            posts = postService.searchPostsSortedNew(search);
        }
        else{
            posts = postService.getAllPost();
        }
        model.addAttribute("posts", posts);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("search", search);
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("allAuthors", userService.getAuthors());
        return "home";
    }

    @GetMapping("/readPost/{id}")
    public String readPost(Model model, @PathVariable int id){
        Post post = postService.getPostById(id);
        model.addAttribute("post",post);
        model.addAttribute("comment", new Comment());
        return "readPost";
    }

    @GetMapping("/updatePost/{id}")
    public String updatePost(@PathVariable("id") int id, Model model){
        Post post = postService.getPostById(id);
        List<Tag> tagsList = post.getTags();
        StringBuilder tags = new StringBuilder();
        for(Tag tag: tagsList){
            tags.append(tag.getName()).append(",");
        }
        if (tags.length() > 0){
            tags.deleteCharAt(tags.length() - 1);
        }

        model.addAttribute("post",post);
        model.addAttribute("tag",tags.toString());

        return "addPost";
    }

    @GetMapping("/deletePost/{id}")
    public String deletePost(@PathVariable("id") int id){
        postService.deletePostById(id);
        return "redirect:/home";
    }
}
