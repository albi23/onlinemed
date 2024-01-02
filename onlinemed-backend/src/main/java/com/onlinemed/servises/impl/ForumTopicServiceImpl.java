package com.onlinemed.servises.impl;

import com.onlinemed.model.ForumPost;
import com.onlinemed.model.ForumPost_;
import com.onlinemed.model.ForumTopic;
import com.onlinemed.model.ForumTopic_;
import com.onlinemed.servises.api.ForumTopicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Service
public class ForumTopicServiceImpl extends BaseObjectServiceImpl<ForumTopic> implements ForumTopicService {


    @Override
    public List<ForumTopic> findTopics(String sortBy, Boolean ascending, Integer pageNumber, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ForumTopic> criteriaQuery = criteriaBuilder.createQuery(ForumTopic.class);
        Root<ForumTopic> root = criteriaQuery.from(ForumTopic.class);
        addOrderByCondition(criteriaBuilder, criteriaQuery, root, sortBy, ascending);
        TypedQuery<ForumTopic> typedQuery = entityManager.createQuery(criteriaQuery);
        addPagination(typedQuery, pageNumber, pageSize);
        return typedQuery.getResultList();
    }

    public List<ForumTopic> getCategoryTopics(UUID categoryId) {
        final EntityManager entityManager = this.getEntityManager();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<ForumTopic> query = criteriaBuilder.createQuery(ForumTopic.class);
        final Root<ForumTopic> root = query.from(ForumTopic.class);
        query.where(criteriaBuilder.equal(root.get(ForumTopic_.FORUM_CATEGORY_ID), categoryId));
        return entityManager.createQuery(query).getResultList();
    }


    @Transactional
    @Override
    public void delete(UUID id) {
        final ForumTopic entity = this.find(id);
        final List<ForumPost> childPost = this.findChildPost(id);
        for (ForumPost forumPost : childPost) {
            this.getEntityManager().remove(forumPost);
        }
        this.getEntityManager().remove(entity);
    }

    private List<ForumPost> findChildPost(UUID id) {
        final EntityManager entityManager = this.getEntityManager();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<ForumPost> query = criteriaBuilder.createQuery(ForumPost.class);
        final Root<ForumPost> root = query.from(ForumPost.class);
        query.where(criteriaBuilder.equal(root.get(ForumPost_.FORUM_TOPIC_ID), id));
        return entityManager.createQuery(query).getResultList();
    }
}
