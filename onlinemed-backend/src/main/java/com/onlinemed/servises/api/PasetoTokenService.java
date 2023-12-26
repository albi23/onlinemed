package com.onlinemed.servises.api;

import com.onlinemed.model.Person;

import java.util.Optional;

public interface PasetoTokenService {
    //Tokens used for request authorization
    String generateRequestToken(String subject);

    Optional<String> parseRequestToken(String token);

    boolean requestTokenValid(String token);

    Optional<Person> getPersonFromRequestToken(String token);



}
