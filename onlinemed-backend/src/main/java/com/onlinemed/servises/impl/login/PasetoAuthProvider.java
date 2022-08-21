package com.onlinemed.servises.impl.login;

import com.onlinemed.model.Person;
import com.onlinemed.servises.api.PersonService;
import com.onlinemed.servises.api.SecurityService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("pasetoAuthProvider")
public class PasetoAuthProvider implements AuthenticationProvider {

    private final PersonService personService;

    private final SecurityService securityService;

    private Person authenticatedPerson;

    public PasetoAuthProvider(PersonService personService, SecurityService securityService) {
        this.personService = personService;
        this.securityService = securityService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userName = authentication.getName().trim();
        String password = authentication.getCredentials().toString().trim();
        if (password.length() == 0) {
            throw new BadCredentialsException("model.emptyPassword");
        }
        authenticatedPerson = personService.findPersonByUsername(userName);
        if (authenticatedPerson == null) {
            throw new BadCredentialsException("model.incorrectCredential");
        }

        if (!securityService.isPasswordCorrect(authenticatedPerson.getId(), password)) {
            throw new BadCredentialsException("model.incorrectCredential");
        }

        Set<GrantedAuthority> authorities =
                personService.getAuthorities(authenticatedPerson.getRoles());

        return new UsernamePasswordAuthenticationToken(authenticatedPerson, null, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }


    public Person getAuthenticatedPerson() {
        return authenticatedPerson;
    }
}
