package com.onlinemed.servises.impl;

import com.onlinemed.model.ForumPost;
import com.onlinemed.model.ForumPost_;
import com.onlinemed.model.Person;
import com.onlinemed.model.Person_;
import com.onlinemed.servises.api.ForumPostService;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForumPostServiceImpl extends BaseObjectServiceImpl<ForumPost> implements ForumPostService {

    @Transactional
    @Override
    public List<ForumPost> findPostFromTopic(UUID topicId, String sortBy, Boolean ascending,
                                             Integer pageNumber, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ForumPost> criteriaQuery = criteriaBuilder.createQuery(ForumPost.class);
        Root<ForumPost> postRoot = criteriaQuery.from(ForumPost.class);

        // lazy fetch thing
        final Join<ForumPost, Person> personJoin = postRoot.join(ForumPost_.postCreator);
        personJoin.join(Person_.community);

        criteriaQuery.where(criteriaBuilder.equal(postRoot.get(ForumPost_.FORUM_TOPIC_ID), topicId));
        addOrderByCondition(criteriaBuilder, criteriaQuery, postRoot, sortBy, ascending);
        TypedQuery<ForumPost> typedQuery = entityManager.createQuery(criteriaQuery);
        addPagination(typedQuery, pageNumber, pageSize);
        return typedQuery.getResultList();
    }

    @Override
    public Long getTopicPostCount(UUID topicId, String sortBy, Boolean ascending, Integer pageNumber, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<ForumPost> postRoot = criteriaQuery.from(ForumPost.class);
        criteriaQuery.where(criteriaBuilder.equal(postRoot.get(ForumPost_.FORUM_TOPIC_ID), topicId));
        criteriaQuery.select(criteriaBuilder.countDistinct(postRoot));
        final TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        return Optional.of(typedQuery.getSingleResult()).orElse(0L);
    }


}
