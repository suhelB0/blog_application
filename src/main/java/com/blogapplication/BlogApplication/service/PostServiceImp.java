package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.Post;
import com.blogapplication.BlogApplication.Entity.Tag;
import com.blogapplication.BlogApplication.Entity.User;
import com.blogapplication.BlogApplication.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImp implements PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Override
    public Post getPostById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    @Override
    public void saveOrUpdatePost(Post post, String tagString) {
        if(post.getId()!=0){
            Post existingPost = getPostById(post.getId());
            post.setCreatedAt(existingPost.getCreatedAt());
            post.setUpdatedAt(LocalDateTime.now());
            post.setComments(existingPost.getComments());
        }
        else{
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());
        }

        User user = userService.getUserById(1);
        post.setUser(user);

        String tags[] = tagString.split(",");
        List<Tag> tagList = new ArrayList<>();
        for(String tag: tags){
            tag = tag.trim();
            Tag existingTag = tagService.findTagByName(tag);
            if(existingTag == null){
                Tag newTag = new Tag();
                newTag.setName(tag);
                tagService.saveTag(newTag);
                tagList.add(newTag);
            }
            else{
                tagList.add(existingTag);
            }
        }

        String excerpt = post.getContent();
        if(excerpt.length()>200){
            post.setExcerpt(excerpt.substring(0,200));
        }
        else{
            post.setExcerpt(excerpt);
        }
        post.setTags(tagList);
        post.setPublished(true);

        postRepository.save(post);
    }

    @Override
    public void deletePostById(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> searchPosts(String keyword) {
        return postRepository.searchPosts(keyword);
    }

    @Override
    public List<Post> getAllPostsSorted(String sortBy) {
        Sort sort = Sort.by("createdAt");
        if("1".equals(sortBy)) {
            sort = sort.ascending();
        } else if("2".equals(sortBy)) {
            sort = sort.descending();
        }
        return postRepository.findAll(sort);
    }

    @Override
    public List<Post> getAllPostsSortedOld() {
        return postRepository.findAllByOrderByCreatedAtAsc();
    }

    @Override
    public List<Post> getAllPostsSortedNew() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Post> searchPostsSortedOld(String search) {
        return postRepository.searchPostsSortedOld(search);
    }

    @Override
    public List<Post> searchPostsSortedNew(String search) {
        return postRepository.searchPostsSortedNew(search);
    }
}
