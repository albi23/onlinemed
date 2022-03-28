package com.onlinemed.servises.api;

import com.blueveery.core.services.BaseService;
import com.onlinemed.config.exceptions.ValidationException;
import com.onlinemed.model.BaseObject;
import com.onlinemed.model.dao.SortByCondition;
import com.onlinemed.model.dto.Violation;
import com.onlinemed.model.translations.Language;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;

public interface BaseObjectService<E extends BaseObject> extends BaseService<E> {

    int defaultPageNumber = 0;
    int defaultPageSize = 5;

    Long countAll();

    List<E> findAllFromListById(List<UUID> uuidList);

    /**
     * @param criteriaBuilder {@code CriteriaBuilder} object
     * @param criteriaQuery   {@code CriteriaQuery} object
     * @param root            represents a bound type, usually an entity that appears in
     * @param sortBy          string representation of object that is used to sort
     * @param ascending       boolean value, if true result list will have ascending order
     *                        Method for counting storage request per page
     */
    default void addOrderByCondition(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, From root, String sortBy, Boolean ascending) {
        SortByCondition sortByCondition = createSortByCondition(sortBy);
        if (sortByCondition != null && sortByCondition.getSortByField() != null) {
            setUpOrderByCondition(criteriaBuilder, criteriaQuery, root, sortByCondition.getSortByField(), sortByCondition.getSortByConnectedObjectField(), ascending);
        }
    }

    /**
     * @param sortBy string representation of object that is used to sort
     * @return {@code SortByCondition} object
     * Method for counting storage request per page or null (if parameter is empty)
     */
    default SortByCondition createSortByCondition(String sortBy) {
        if (sortBy != null && !sortBy.equals("")) {
            SortByCondition sortByCondition = new SortByCondition();
            String[] sortByParts = sortBy.split("/");
            sortByCondition.setSortByField(sortByParts[0]);
            return sortByCondition;
        }
        return null;
    }

    /**
     * @param criteriaBuilder            {@code CriteriaBuilder} object
     * @param criteriaQuery              {@code CriteriaQuery} object
     * @param root                       represents a bound type, usually an entity that appears in
     * @param sortByField                string representation of object that is used to sort
     * @param sortByConnectedObjectField string representation of object that is used to sort
     * @param ascending                  boolean value, if true result list will have ascending order
     *                                   Method for counting storage request per page
     */
    default void setUpOrderByCondition(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, From root, String sortByField, String sortByConnectedObjectField, Boolean ascending) {
        Class entityType = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

        SingularAttribute sortBySingularAttribute;
        if (sortByConnectedObjectField == null) {
            sortBySingularAttribute = getSingularAttributeByClass(entityType, sortByField);
            if (sortBySingularAttribute != null) {
                doSetUpOrderByCondition(criteriaBuilder, criteriaQuery, ascending, root.get(sortBySingularAttribute));
            }
        } else {
            SingularAttribute rootSingularAttribute = getSingularAttributeByClass(entityType, sortByField);
            if (rootSingularAttribute != null) {
                sortBySingularAttribute = getSingularAttributeByClass(rootSingularAttribute.getJavaType(), sortByConnectedObjectField);
                if (sortBySingularAttribute != null) {
                    doSetUpOrderByCondition(criteriaBuilder, criteriaQuery, ascending, root.get(rootSingularAttribute).get(sortBySingularAttribute));
                }
            }
        }
    }

    /**
     * @param criteriaBuilder {@code CriteriaBuilder} object
     * @param criteriaQuery   {@code CriteriaQuery} object
     * @param ascending       boolean value, if true result list will have ascending order
     * @param sortByPath      string representation of object that is used to sort
     */
    default void doSetUpOrderByCondition(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, Boolean ascending, Path sortByPath) {
        if (ascending == null || ascending) {
            criteriaQuery.orderBy(criteriaBuilder.asc(sortByPath));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(sortByPath));
        }
    }

    /**
     * @param sortByClassName string representation of object that is used to sort
     * @return {@code Class} represented by parameter sortByClassName
     */
    default Class<?> getMetaModelClass(String sortByClassName) {
        Class<?> metaModelClass;
        try {
            metaModelClass = Class.forName(BaseObject.class.getPackage().getName() + "." + sortByClassName + "_");
        } catch (ClassNotFoundException e) {
            try {
                metaModelClass = Class.forName(Language.class.getPackage().getName() + "." + sortByClassName + "_");
            } catch (ClassNotFoundException e1) {
                throw new ValidationException(new Violation("model.validation.metaModelClassNotFound"));
            }
        }
        return metaModelClass;
    }

    /**
     * @param metaModelClass type of object that is used to sort
     * @param sortByField    string representation of object that is used to sort
     * @return {@code SingularAttribute} object
     * Get the type SingularAttribute, that represents persistent single-valued properties or fields.
     */
    default SingularAttribute getSingularAttributeByClass(Class<?> metaModelClass, String sortByField) {
        if (metaModelClass != null) {
            for (Field field : metaModelClass.getDeclaredFields()) {
                if (field.getName().equals(sortByField)) {
                    try {
                        return (SingularAttribute) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new ValidationException(new Violation("model.validation.wrongSortByParameter"));
                    }
                }
            }
        }
        return null;
    }
}
