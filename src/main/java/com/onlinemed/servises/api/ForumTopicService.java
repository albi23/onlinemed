package com.onlinemed.servises.api;

import com.onlinemed.model.ForumTopic;

import java.util.List;
import java.util.UUID;

public interface ForumTopicService extends BaseObjectService<ForumTopic> {

    List<ForumTopic> findTopics(String sortBy, Boolean ascending, Integer pageNumber, Integer pageSize);

    List<ForumTopic> getCategoryTopics(UUID categoryId);
}
