package com.onlinemed.servises.api;

import com.onlinemed.model.Person;
import com.onlinemed.model.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PersonService extends BaseObjectService<Person> {

    Optional<Person> findPersonByUsername(String username);

    Optional<Person> findPersonByUsernameWithTouchRoles(String username);

    int updatePersonLastLogin(UUID personID);

    List<Person> findPeople(String sortBy, Boolean ascending, Integer pageNumber, Integer pageSize);

    Set<GrantedAuthority> getAuthorities(List<Role> personRoles);

}
