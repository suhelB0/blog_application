package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.Tag;
import com.blogapplication.BlogApplication.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImp implements TagService{
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImp(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public void saveTag(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public Tag findTagByName(String tagName) {
        return tagRepository.findByName(tagName);
    }

    public List<Tag> getPublishedTags() {
        return tagRepository.findTagsOfPublishedPosts();
    }
}
