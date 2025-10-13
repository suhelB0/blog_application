package com.blogapplication.BlogApplication.service;

import com.blogapplication.BlogApplication.Entity.Tag;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();

    void saveTag(Tag tag);

    Tag findTagByName(String tagName);

    List<Tag> getPublishedTags();
}
