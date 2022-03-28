package com.onlinemed.controllers;

import com.blueveery.core.ctrls.DeleteObjectCtrl;
import com.blueveery.core.ctrls.GetObjectCtrl;
import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.model.BaseEntity;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.config.exceptions.ValidationException;
import com.onlinemed.config.generator.GeneratorIgnore;
import com.onlinemed.controllers.core.CountAll;
import com.onlinemed.model.*;
import com.onlinemed.model.dto.Violation;
import com.onlinemed.servises.api.PersonService;
import com.onlinemed.servises.api.RegistrationLinkService;
import com.onlinemed.servises.api.RoleService;
import com.onlinemed.servises.api.SecurityService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.onlinemed.model.SystemFunctionalities.USER_MANAGEMENT;

/**
 * Class used to define methods that operate on objects of class Person
 */
@RestController
@RequestMapping("/api/person")
@JsonScope(positive = true, scope = {Person.class, Role.class})
public class PersonCtrl implements GetObjectCtrl<Person>, CountAll<Person>,
        DeleteObjectCtrl<Person>, UpdateObjectCtrl<Person> {

    final private PersonService personService;
    final private RegistrationLinkService registrationLinkService;
    final private SecurityService securityService;
    final private RoleService roleService;

    public PersonCtrl(PersonService personService,
                      RegistrationLinkService registrationLinkService,
                      SecurityService securityService,
                      RoleService roleService) {
        this.personService = personService;
        this.registrationLinkService = registrationLinkService;
        this.securityService = securityService;
        this.roleService = roleService;
    }

    @Override
    public BaseService<Person> getService() {
        return personService;
    }


    @JsonScope(positive = true, scope = {Person.class, Role.class})
    @Transactional
    @RequestMapping(path = "/getPeople", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public List<Person> getPeople(@RequestParam(value = "sortBy", required = false) String sortBy,
                                  @RequestParam(value = "ascending", required = false) Boolean ascending,
                                  @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                  @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<Person> people = personService.findPeople(sortBy, ascending, pageNumber, pageSize);
        touchPersonList(people);
        return people;
    }

    @Transactional
    @JsonScope(positive = true, scope = {Person.class, DoctorInfo.class, FacilityLocation.class})
    @RequestMapping(path = "/get-doctor-info/{id}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public Person getDoctorInfoFromPerson(@PathVariable("id") UUID personId) {
        final Person person = this.personService.find(personId);
        final DoctorInfo doctorInfo = person.getDoctorInfo();
        if (doctorInfo != null)
            doctorInfo.getFacilityLocations().forEach(f -> f.getVisitsPriceList().forEach((k, v) -> {
            }));
        return person;
    }

    @Secured({USER_MANAGEMENT})
    @RequestMapping(path = {"/{id}"}, method = {RequestMethod.PUT}, consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    @Override
    public Person updateObject(@RequestBody BaseEntity entity) {
        final Person merge = this.getService().merge((Person) entity);
        this.touchPersonList(List.of(merge));
        return merge;
    }

    @RequestMapping(path = {"/usage-username/"}, method = {RequestMethod.GET})
    @ResponseBody
    public boolean isUsernameUsed(@RequestParam("username") String username) {
        return this.personService.findPersonByUsername(username) != null;
    }

    @JsonScope(positive = true, scope = {Person.class, Role.class})
    @Override
    public Person doGetObject(UUID id) {
        final Person person = this.getService().find(id);
        person.getRoles().forEach(BaseObject::touchObject);
        return person;
    }

    private void touchPersonList(List<Person> people) {
        people.forEach(person -> {
            person.touchObject();
            person.getRoles().forEach(BaseObject::touchObject);
        });
    }

    @Secured({USER_MANAGEMENT})
    @RequestMapping(path = {"/{id}"}, method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void deleteObject(@PathVariable("id") UUID id) {
        this.doDeleteObject(id);
    }


    @RequestMapping(method = {RequestMethod.POST},
            path = "/{registerId}/register",
            consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    @JsonScope(positive = true, scope = {Person.class, Role.class, Community.class, DoctorInfo.class, FacilityLocation.class})
    @GeneratorIgnore
    @ResponseStatus(HttpStatus.CREATED)
    public Person createObject(HttpServletRequest request,
                               @RequestBody Person person,
                               @PathVariable("registerId") UUID id,
                               @RequestParam String rolesType) {
        final String base64 = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        final String plainPassword = new String(Base64.getDecoder().decode(base64));
        if (this.registrationLinkService.find(id).getId() != id) {
            throw new ValidationException(new Violation("model.incorrect-registration-link"));
        }
        person.setSecurity(new Security(person, securityService.hashPassword(plainPassword)));
        final Community c = person.getCommunity();
        person.setCommunity(new Community(c.getComments(), c.getDescription(), c.getTimestamp(), c.getLastLogin(), person));

        final List<Integer> roleTypes = Arrays.stream(rolesType.split(";")).map(Integer::parseInt).collect(Collectors.toList());
        final List<Role> all = this.roleService.findAll();
        final List<Role> roles =
                roleTypes.stream().map(intValue -> all.stream().filter(role -> role.getRoleType().ordinal() == intValue).findFirst().get())
                        .collect(Collectors.toList());
        person.setRoles(roles);
        final Person merge = this.personService.merge(person);
        this.registrationLinkService.delete(id);
        return merge;
    }
}
