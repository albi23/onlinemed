package com.onlinemed.servises.api;

import com.onlinemed.model.Person;
import com.onlinemed.model.Security;

import java.util.UUID;

public interface SecurityService extends BaseObjectService<Security> {

    boolean isPasswordCorrect(UUID personID, String password);

    boolean validatePassword(String plainPassword, String hashedPassword);

    String hashPassword(String stringPwd);

    Security findUserSecurity(UUID personID);

    Security createSecurity(Person person, String password);

}
