package com.onlinemed.servises.api;

import com.onlinemed.model.Person;
import net.aholbrook.paseto.service.Token;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface PasetoTokenService {
    //Tokens used for request authorization
    String generateRequestToken(String subject);

    Token parseRequestToken(String token);

    boolean requestTokenValid(String token);

    Optional<Person> getPersonFromRequestToken(HttpServletRequest request);

    //Tokens used for changes in security entity
    String generateSecurityToken(String subject);

    Token parseSecurityToken(String token);

    boolean securityTokensValid(Person person, String token);
}
