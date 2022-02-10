package com.onlinemed.servises.api;

import com.onlinemed.model.Person;
import com.onlinemed.model.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface PersonService extends BaseObjectService<Person> {

    Person findPersonByUsername(String username);

    Person findPersonByUsernameWithTouchRoles(String username);

    List<Person> findPeople(String sortBy, Boolean ascending, Integer pageNumber, Integer pageSize);

    Set<GrantedAuthority> getAuthorities(List<Role> personRoles);

}
