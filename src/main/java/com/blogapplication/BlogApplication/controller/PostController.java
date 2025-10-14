package com.blogapplication.BlogApplication.controller;

import com.blogapplication.BlogApplication.Entity.Comment;
import com.blogapplication.BlogApplication.Entity.Post;
import com.blogapplication.BlogApplication.Entity.Tag;
import com.blogapplication.BlogApplication.service.PostService;
import com.blogapplication.BlogApplication.service.TagService;
import com.blogapplication.BlogApplication.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {
    private final PostService postService;
    private final TagService tagService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }

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
                       @RequestParam(value = "sortField", defaultValue = "publishedAt", required = false) String sortField,
                       @RequestParam(value = "order", defaultValue = "", required = false) String order,
                       @RequestParam(value = "tag", required = false) String[] tags,
                       @RequestParam(value = "author", required = false) String[] authors,
                       @RequestParam(value = "start", defaultValue = "0") int start,
                       @RequestParam(value = "limit", defaultValue = "6") int limit, Model model){

        Page<Post> postPage = postService.getFilteredPosts(search, sortField, order, tags, authors, start, limit);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", start);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("order", order);
        model.addAttribute("search", search);
        model.addAttribute("tag", tags);
        model.addAttribute("author", authors);
        model.addAttribute("tags", tagService.getPublishedTags());
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
