package com.onlinemed.model;

import com.onlinemed.config.converters.RoleTypeListConverter;
import com.onlinemed.model.enums.RoleType;

import javax.persistence.*;
import java.util.List;

/**
 * The class responsible for creating the registration_link table and mapping data to the form
 * of RegistrationLink objects
 */
@Entity
@Table(name = "registration_link")
@AttributeOverrides(value = @AttributeOverride(name = "timestamp", column = @Column(name = "expiration_time")))
public class RegistrationLink extends BaseObject{

    /**
     * Do not update after changes
     */
    public void updateTimestamp() {
    }

    @Convert(converter = RoleTypeListConverter.class)
    private List<RoleType> roleType;

    public RegistrationLink() {
    }

    public List<RoleType> getRoleType() {
        return roleType;
    }

    /**
     * Sets <code>RegistrationLink</code> roleType value
     *
     * @param roleType - set new value of roleType
     */
    public void setRoleType(List<RoleType> roleType) {
        this.roleType = roleType;
    }
}
