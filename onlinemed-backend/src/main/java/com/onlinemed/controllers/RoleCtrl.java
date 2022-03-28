package com.onlinemed.controllers;

import com.blueveery.core.ctrls.FindAllCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.controllers.core.CountAll;
import com.onlinemed.model.Person;
import com.onlinemed.model.Role;
import com.onlinemed.servises.api.RoleService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.onlinemed.model.SystemFunctionalities.USER_MANAGEMENT;

/**
 * Class used to define methods that operate on objects of class Role
 */
@RestController
@RequestMapping("/api/role")
@Secured({USER_MANAGEMENT})
@JsonScope(positive = true, scope = {Person.class})
public class RoleCtrl implements FindAllCtrl<Role>, CountAll<Role> {

    private final RoleService roleService;

    public RoleCtrl(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public BaseService<Role> getService() {
        return roleService;
    }
}
