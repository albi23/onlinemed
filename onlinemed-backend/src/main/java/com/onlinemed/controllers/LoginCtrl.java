package com.onlinemed.controllers;

import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.config.exceptions.ValidationException;
import com.onlinemed.config.generator.GeneratorIgnore;
import com.onlinemed.model.BaseObject;
import com.onlinemed.model.Functionality;
import com.onlinemed.model.Notification;
import com.onlinemed.model.Person;
import com.onlinemed.model.Role;
import com.onlinemed.model.Visit;
import com.onlinemed.model.dto.Violation;
import com.onlinemed.servises.api.PasetoTokenService;
import com.onlinemed.servises.api.PersonService;
import com.onlinemed.servises.impl.login.PasetoAuthProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

/**
 * A class used to define the methods that manage the login process
 */
@RestController
@RequestMapping("/api/login")
@JsonScope(positive = true, scope = {Person.class})
public class LoginCtrl implements UpdateObjectCtrl<BaseObject> {

    private final PasetoTokenService pasetoTokenService;
    private final PersonService personService;
    private final PasetoAuthProvider pasetoAuthProvider;

    public LoginCtrl(PasetoTokenService pasetoTokenService,
                     PersonService personService,
                     PasetoAuthProvider pasetoAuthProvider) {
        this.pasetoTokenService = pasetoTokenService;
        this.personService = personService;
        this.pasetoAuthProvider = pasetoAuthProvider;
    }

    @Override
    public BaseService<BaseObject> getService() {
        throw new UnsupportedOperationException("Not allowed operation");
    }

    @GeneratorIgnore
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @JsonScope(positive = true, scope = {Person.class, Role.class, Functionality.class, Notification.class, Visit.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request) {
        final String s = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        final String[] pwd = new String(Base64.getDecoder().decode(s)).split(":");
        Authentication authentication =
                pasetoAuthProvider.authenticate(new UsernamePasswordAuthenticationToken(pwd[0], pwd[1]));
        if (authentication.isAuthenticated()) {
            Map<String, Object> model = new HashMap<>();
            final Person authenticatedPerson = pasetoAuthProvider.getAuthenticatedPerson();
            authenticatedPerson.getNotifications().forEach(BaseObject::touchObject);
            model.put("person", authenticatedPerson);
            model.put("token", pasetoTokenService.generateRequestToken(pwd[0]));
            this.personService.updatePersonLastLogin(authenticatedPerson.getId());
            return ok(model);
        } else {
            throw new ValidationException(new Violation("model.incorrect-credential"));
        }
    }
}
