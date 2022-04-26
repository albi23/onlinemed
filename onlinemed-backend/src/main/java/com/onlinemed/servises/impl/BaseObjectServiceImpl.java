package com.onlinemed.servises.impl;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.core.services.BaseServiceImpl;
import com.onlinemed.config.exceptions.ValidationException;
import com.onlinemed.model.BaseObject;
import com.onlinemed.model.dto.Violation;
import com.onlinemed.servises.api.BaseObjectService;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings({"unchecked"})
public class BaseObjectServiceImpl<E extends BaseObject> extends BaseServiceImpl<E> implements BaseObjectService<E> {

    private final Class entityType = (Class<?>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * The method responsible for matching the returned results to the size of the result table on the UI user side
     *
     * @param typedQuery - query
     * @param pageNumber - pageNumber
     * @param pageSize   - pageSize
     */
    public <T extends BaseEntity> void addPagination(TypedQuery<T> typedQuery, Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null) ? defaultPageNumber : pageNumber;
        pageSize = (pageSize == null) ? defaultPageSize : pageSize;
        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
    }

    @Override
    @Transactional(readOnly = true, noRollbackFor = {ClassNotFoundException.class})
    public Long countAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(this.entityType);
        criteriaQuery.select(criteriaBuilder.countDistinct(root));
        TypedQuery<Long> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
        return Optional.of(typedQuery.getSingleResult()).orElse(0L);
    }

    @Transactional(readOnly = true, noRollbackFor = {ClassNotFoundException.class})
    public List<E> findAllFromListById(List<UUID> uuidList) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(this.entityType);
        Root<E> root = criteriaQuery.from(this.entityType);
        Metamodel m = getEntityManager().getMetamodel();
        EntityType<E> E_ = m.entity(this.entityType);
        Predicate predicate = root.get(E_.getId(UUID.class)).in(uuidList);
        return getEntityManager().createQuery(criteriaQuery.where(predicate)).getResultList();
    }

    @Override
    public SingularAttribute getSingularAttributeByClass(Class<?> metaModelClass, String sortByField) {
        try {
            Metamodel m = getEntityManager().getMetamodel();
            EntityType E_;
            try {
                E_ = m.entity(metaModelClass);
            } catch (IllegalArgumentException ex) {
                throw new ClassNotFoundException("Class not found in model");
            }
            return E_.getSingularAttribute(sortByField);
        } catch (java.lang.IllegalArgumentException ex) {
            throw new ValidationException(new Violation("Wrong sortByField param "));
        } catch (ClassNotFoundException ex2) {
            throw new ValidationException(new Violation(ex2.getMessage()));
        }
    }
}