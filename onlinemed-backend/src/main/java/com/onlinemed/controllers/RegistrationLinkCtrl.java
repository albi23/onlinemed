package com.onlinemed.controllers;

import com.blueveery.core.ctrls.DeleteObjectCtrl;
import com.blueveery.core.ctrls.GetObjectCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.RegistrationLink;
import com.onlinemed.servises.api.RegistrationLinkService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.onlinemed.model.SystemFunctionalities.USER_MANAGEMENT;

@PermitAll
@RestController
@RequestMapping("/api/registration-link")
@JsonScope(positive = true, scope = {RegistrationLink.class})
public class RegistrationLinkCtrl implements GetObjectCtrl<RegistrationLink>, DeleteObjectCtrl<RegistrationLink> {


    private final RegistrationLinkService registrationLinkService;

    public RegistrationLinkCtrl(RegistrationLinkService registrationLinkService) {
        this.registrationLinkService = registrationLinkService;
    }

    @Override
    public BaseService<RegistrationLink> getService() {
        return this.registrationLinkService;
    }


    @Secured({USER_MANAGEMENT})
    @RequestMapping(method = {RequestMethod.POST}, consumes = {"application/json"},
            produces = {"application/json"}, path = "/group")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @JsonScope(positive = true, scope = {RegistrationLink.class})
    public List<RegistrationLink> createRegistrationLinks(@RequestBody List<RegistrationLink> registrationLinks) {
        return registrationLinks.stream().map(r -> this.getService().merge(r)).collect(Collectors.toList());
    }


    @Secured({USER_MANAGEMENT})
    @RequestMapping(method = {RequestMethod.GET}, path = "/server/ip")
    @ResponseBody
    public String getServerIp() {
        return this.registrationLinkService.getLocalAdres();
    }


    @Override
    public RegistrationLink doGetObject(UUID id) {
        try {
            return this.getService().find(id);
        } catch (NoResultException ex) {
            return new RegistrationLink();
        }
    }
}
