package com.onlinemed.model;

import com.onlinemed.model.enums.RoleType;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * The class responsible for creating the role table and mapping data to the form
 * of Role objects
 */
@Entity
@Table(name = "role")
@DynamicUpdate
public class Role extends BaseObject {

    /**
     * Default role type is set to User Role
     */
    private RoleType roleType = RoleType.USER;


    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "role_x_functionality",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "functionality_id")}
    )
    private List<Functionality> functionalities = new ArrayList<>();


    public Role() {
    }

    public Role(RoleType roleType, List<Functionality> functionalities) {
        this.roleType = roleType;
        this.functionalities = functionalities;
    }

    /**
     * Gets roleType
     *
     * @return value of roleType field
     */
    public RoleType getRoleType() {
        return roleType;
    }

    /**
     * Sets <code>Role</code> roleType value
     *
     * @param roleType - set new value of roleType
     */
    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    /**
     * Gets functionalities
     *
     * @return value of functionalities field
     */
    public List<Functionality> getFunctionalities() {
        return functionalities;
    }

    /**
     * Sets <code>Role</code> functionalities value
     *
     * @param functionalities - set new value of functionalities
     */
    public void setFunctionalities(List<Functionality> functionalities) {
        this.functionalities = functionalities;
    }

    @Override
    public String toString() {
        return "Role{" +
                "[" + roleType +
                "], version=" + super.getVersion() +
                '}';
    }

}
