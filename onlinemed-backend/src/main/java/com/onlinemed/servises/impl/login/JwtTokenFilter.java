package com.onlinemed.servises.impl.login;

import com.onlinemed.model.Person;
import com.onlinemed.servises.api.PasetoTokenService;
import com.onlinemed.servises.api.PersonService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static com.onlinemed.servises.impl.login.SecurityConstants.TOKEN_HEADER;

/**
 * Authorization filter to validate requests containing paseto token
 */
@Service
public class JwtTokenFilter extends OncePerRequestFilter {

    final PasetoTokenService pasetoTokenService;
    private final PersonService personService;

    public JwtTokenFilter(PasetoTokenService pasetoTokenService,
                          PersonService personService) {
        this.pasetoTokenService = pasetoTokenService;
        this.personService = personService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String token = req.getHeader(TOKEN_HEADER);
        if (token == null) {
            chain.doFilter(req, res);
            return;
        }
        if (pasetoTokenService.requestTokenValid(token)) {
            Optional<Person> personOpt = pasetoTokenService.getPersonFromRequestToken(token);
            personOpt.ifPresent(person -> {
                UsernamePasswordAuthenticationToken authentication = getAuthentication(person);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                res.addHeader(TOKEN_HEADER, pasetoTokenService.generateRequestToken(
                        person.getUserName()));
            });
        }
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(Person person) {
        if (person != null) {
            Set<GrantedAuthority> authorities = personService.getAuthorities(person.getRoles());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(person, null, authorities);
            authentication.setDetails(person.getRoles());
            return authentication;
        }
        return null;
    }
}
