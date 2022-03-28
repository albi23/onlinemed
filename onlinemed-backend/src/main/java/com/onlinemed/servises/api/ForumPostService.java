package com.onlinemed.servises.api;

import com.onlinemed.model.ForumPost;

import java.util.List;
import java.util.UUID;

public interface ForumPostService extends BaseObjectService<ForumPost> {

    List<ForumPost> findPostFromTopic(UUID topicId, String sortBy, Boolean ascending, Integer pageNumber, Integer pageSize);

    Long getTopicPostCount(UUID topicId, String sortBy, Boolean ascending, Integer pageNumber, Integer pageSize);

}
