package com.blogapplication.BlogApplication.api;

import com.blogapplication.BlogApplication.Entity.Post;
import com.blogapplication.BlogApplication.Entity.User;
import com.blogapplication.BlogApplication.service.PostService;
import com.blogapplication.BlogApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/blog")
public class PostApiController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/posts", consumes = "application/json")
    public ResponseEntity<String> createPost(@RequestBody Post post, @RequestParam(value = "tags", required = false) String tags){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAuthenticated = !"anonymousUser".equals(username);

        if(!isAuthenticated){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must to be logged in to create a post");
        }
        postService.saveOrUpdatePost(post, tags);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created");
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> readPost(@PathVariable("id") int id){
        Post post = postService.getPostById(id);
        if(post == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @GetMapping("/posts")
    public List<Post> home(@RequestParam(value = "search", defaultValue = "", required = false) String search,
                           @RequestParam(value = "sortField", defaultValue = "publishedAt", required = false) String sortField,
                           @RequestParam(value = "order", defaultValue = "", required = false) String order,
                           @RequestParam(value = "tag", required = false) String[] tags,
                           @RequestParam(value = "author", required = false) String[] authors,
                           @RequestParam(value = "start", defaultValue = "0") int start,
                           @RequestParam(value = "limit", defaultValue = "10") int limit){

        Page<Post> postPage = postService.getFilteredPosts(search, sortField, order, tags, authors, start, limit);

        return postPage.getContent();
    }

    @PutMapping(value = "/posts/{id}", consumes = "application/json")
    public ResponseEntity<String> updatePost(
            @PathVariable("id") int id,
            @RequestBody Post post,
            @RequestParam(value = "tags", required = false) String tags) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAuthenticated = !"anonymousUser".equals(username);
        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update a post");
        }

        Post existingPost = postService.getPostById(id);
        if (existingPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        User user = userService.getUserByEmail(username);
        boolean isAdmin = user.getRoles() != null && user.getRoles().contains("ROLE_ADMIN");
        boolean isAuthor = existingPost.getUser().getId() == user.getId();

        if (!isAdmin && !isAuthor) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this post");
        }
        post.setId(id);
        postService.saveOrUpdatePost(post, tags);

        return ResponseEntity.ok("Post updated successfully");
    }


    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") int id){
        Post post = postService.getPostById(id);
        if (post == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAuthenticated = !"anonymousUser".equals(username);
        if(!isAuthenticated){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must to be logged in to delete post");
        }
        User user = userService.getUserByEmail(username);
        boolean isAdmin = user.getRoles() != null && user.getRoles().contains("ROLE_ADMIN");
        boolean isAuthorOfPost = user.getRoles() != null && post.getUser().getId() == user.getId();

        if(isAdmin || isAuthorOfPost){
            postService.deletePostById(id);
            return ResponseEntity.ok("Post deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete post");
        }
    }
}
