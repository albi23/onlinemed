package com.onlinemed.servises.impl.login;

import com.onlinemed.model.Person;
import com.onlinemed.servises.api.PasetoTokenService;
import com.onlinemed.servises.api.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Set;

import static com.onlinemed.servises.impl.login.SecurityConstants.TOKEN_HEADER;

/**
 * Authorization filter to validate requests containing paseto token
 */
@Service
public class PasetoAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    PasetoTokenService pasetoTokenService;

    @Autowired
    private PersonService personService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        if (req.getHeader(TOKEN_HEADER) == null) {
            chain.doFilter(req, res);
            return;
        }
        if (pasetoTokenService.requestTokenValid(req.getHeader(TOKEN_HEADER))) {
            Person person = pasetoTokenService.getPersonFromRequestToken(req);
            if (person != null) {
                UsernamePasswordAuthenticationToken authentication = getAuthentication(person);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                res.addHeader(TOKEN_HEADER, pasetoTokenService.generateRequestToken(
                        pasetoTokenService.getPersonFromRequestToken(req).getUserName()));
            }
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
