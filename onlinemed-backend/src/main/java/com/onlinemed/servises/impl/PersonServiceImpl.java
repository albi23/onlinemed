package com.onlinemed.servises.impl;

import com.onlinemed.model.BaseObject;
import com.onlinemed.model.Person;
import com.onlinemed.model.Role;
import com.onlinemed.servises.api.PersonService;
import com.onlinemed.servises.impl.login.FunctionalityGrantedAuthority;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "person", cacheManager = "caffeine-manager")
public class PersonServiceImpl extends BaseObjectServiceImpl<Person> implements PersonService {


    @Override
    @Cacheable(key = "#username", cacheNames = "person")
    @Transactional(readOnly = true)
    public Optional<Person> findPersonByUsername(final String username) {
        return entityManager.createNamedQuery(
                        "Person.findPersonByUsernameWithTouchRoles", Person.class)
                .setParameter("username", username)
                .getResultList().stream().findFirst();
    }

    @Override
    @Cacheable(key = "#username", cacheNames = "person")
    @Transactional
    public Optional<Person> findPersonByUsernameWithTouchRoles(String username) {
        final Optional<Person> personByUsername = this.findPersonByUsername(username);
        personByUsername.ifPresent(person ->
                person.getRoles().stream()
                .flatMap(r -> r.getFunctionalities().stream())
                .forEach(BaseObject::touchObject));
        return personByUsername;
    }

    @Override
    @Transactional
    public int updatePersonLastLogin(UUID personID) {
        return entityManager.createNamedQuery("Person.updateLastLoginCommunity")
                .setParameter("id", personID)
                .executeUpdate();
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
