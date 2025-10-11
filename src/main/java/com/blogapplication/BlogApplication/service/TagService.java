package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.Tag;

import java.util.List;

public interface TagService {
    public List<Tag> getAllTags();

    public List<Tag> getAllTagsUnique();

    public void saveTag(Tag tag);

    Tag findTagByName(String tagName);
}
