package com.onlinemed.servises.impl;

import com.onlinemed.model.BaseObject;
import com.onlinemed.model.Person;
import com.onlinemed.model.Person_;
import com.onlinemed.model.Role;
import com.onlinemed.servises.api.PersonService;
import com.onlinemed.servises.impl.login.FunctionalityGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl extends BaseObjectServiceImpl<Person> implements PersonService  {


    @Override
    public Person findPersonByUsername(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Person_.userName), username));
        return entityManager.createQuery(cq).getResultList().stream().findAny().orElse(null);
    }

    @Override
    @Transactional
    public Person findPersonByUsernameWithTouchRoles(String username) {
        final Person personByUsername = this.findPersonByUsername(username);
        personByUsername.getRoles().stream()
                .flatMap(r -> r.getFunctionalities().stream())
                .forEach(BaseObject::touchObject);
        return personByUsername;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities(List<Role> personRoles) {
        return personRoles.stream().map(Role::getFunctionalities)
                .flatMap(Collection::stream)
                .map(FunctionalityGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Person> findPeople(String sortBy, Boolean ascending, Integer pageNumber, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        addOrderByCondition(criteriaBuilder, criteriaQuery, root, sortBy, ascending);
        TypedQuery<Person> typedQuery = entityManager.createQuery(criteriaQuery);
        addPagination(typedQuery, pageNumber, pageSize);
        return typedQuery.getResultList();
    }
}
