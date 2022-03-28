package com.onlinemed.servises.impl;

import com.onlinemed.model.Person;
import com.onlinemed.model.Security;
import com.onlinemed.servises.api.SecurityService;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.UUID;

@Service
public class SecurityServiceImpl extends BaseObjectServiceImpl<Security> implements SecurityService {

    private final Argon2 argon2;
    private static final int DEFAULT_MEMORY = 1 << 12;
    private static final int DEFAULT_PARALLELISM = 1;
    private static final int DEFAULT_ITERATIONS = 3;


    public SecurityServiceImpl() {
        this.argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    }

    @Override
    public String hashPassword(String stringPwd) {
        return argon2.hash(DEFAULT_ITERATIONS, DEFAULT_MEMORY, DEFAULT_PARALLELISM, stringPwd);
    }

    @Override
    public boolean validatePassword(String passwordToCheck, String hashedPassword) {
        return argon2.verify(hashedPassword, passwordToCheck);
    }

    @Transactional
    @Override
    public Security findUserSecurity(UUID personID) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Security> cq = cb.createQuery(Security.class);
        Root<Security> root = cq.from(Security.class);
        cq.select(root);
        cq.where(cb.equal(root.get("person").get("id"), personID));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public Security createSecurity(Person person, String password) {
        Security security = new Security();
        security.setPerson(person);
        security.setHash(hashPassword(password));
        return security;
    }

    @Override
    public boolean isPasswordCorrect(UUID personID, String password) {
        return validatePassword(password, findUserSecurity(personID).getHash());
    }

}
