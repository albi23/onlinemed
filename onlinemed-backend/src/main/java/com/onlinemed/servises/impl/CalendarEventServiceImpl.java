package com.onlinemed.servises.impl;

import com.onlinemed.model.CalendarEvent;
import com.onlinemed.model.CalendarEvent_;
import com.onlinemed.servises.api.CalendarEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Service
public class CalendarEventServiceImpl extends BaseObjectServiceImpl<CalendarEvent> implements CalendarEventService {

    @Transactional(readOnly = true, noRollbackFor = {ClassNotFoundException.class})
    @Override
    public List<CalendarEvent> getUserEvents(UUID personId) {
        final EntityManager entityManager = this.getEntityManager();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<CalendarEvent> query = criteriaBuilder.createQuery(CalendarEvent.class);
        final Root<CalendarEvent> root = query.from(CalendarEvent.class);
        query.where(criteriaBuilder.equal(root.get(CalendarEvent_.PERSON_ID), personId));
        return entityManager.createQuery(query).getResultList();
    }
}
