package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.Post;
import com.blogapplication.BlogApplication.Entity.Tag;
import com.blogapplication.BlogApplication.Entity.User;
import com.blogapplication.BlogApplication.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImp implements PostService{
    private final PostRepository postRepository;
    private final UserService userService;
    private final TagService tagService;

    @Autowired
    public PostServiceImp(PostRepository postRepository, UserService userService, TagService tagService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.tagService = tagService;
    }

    @Override
    public Post getPostById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void saveOrUpdatePost(Post post, String tagString) {
        if(post.getId()!=0){
            Post existingPost = getPostById(post.getId());
            post.setUser(existingPost.getUser());
            post.setComments(existingPost.getComments());
        }
        else{
            User user = userService.getUserById(1);
            post.setUser(user);
        }

        User user = userService.getUserById(1);
        post.setUser(user);

        String[] tags = tagString.split(",");
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
    public Page<Post> getFilteredPosts(String search, String sortField, String order, String[] tags, String[] authors, int start, int limit) {
        boolean hasSearch = search != null && !search.isEmpty();
        boolean hasFilters = tags != null || authors != null;

        Sort sort;
        if(order.equals("asc")){
            sort = Sort.by(Sort.Direction.ASC, sortField);
        }
        else if(order.equals("desc")){
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }
        else{
            sort = Sort.unsorted();
        }

        Pageable pageable = PageRequest.of(start, limit, sort);

        if(hasSearch && hasFilters){
            return postRepository.searchPostsWithFiltersSort(search, tags, authors, pageable);
        }
        else if(hasFilters){
            return postRepository.filterPostsByTagsAndAuthorsSort(tags, authors, pageable);
        }
        else if(hasSearch){
            return postRepository.searchPostsSort(search, pageable);
        }
        else{
            return postRepository.findAllByIsPublishedTrue(pageable);
        }
    }
}
