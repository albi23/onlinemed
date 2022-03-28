package com.onlinemed.model;

import com.blueveery.core.model.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The class responsible for creating the community table and mapping data to the form
 * of Community objects
 */
@Entity
@Table(name = "community")
@AttributeOverrides(value = @AttributeOverride(name = "timestamp", column = @Column(name = "register_date")))
public class Community extends BaseObject {


    private int comments = 0;
    @Column(length = 700)
    private String description = "";
    private LocalDateTime lastLogin = LocalDateTime.now();

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Person user;

    public Community() {
    }


    public Community(int comments, String description, LocalDateTime register, LocalDateTime lastLogin, Person user) {
        this.comments = comments;
        this.description = description;
        this.lastLogin = lastLogin;
        this.user = user;
        this.timestamp = register;
    }

    /**
     * Do not update after changes
     */
    public void updateTimestamp() {
    }

    /**
     * Gets comments
     *
     * @return value of comments field
     */
    public int getComments() {
        return comments;
    }

    /**
     * Sets <code>Community</code> comments value
     *
     * @param comments - set new value of comments
     */
    public void setComments(int comments) {
        this.comments = comments;
    }

    /**
     * Gets description
     *
     * @return value of description field
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets <code>Community</code> description value
     *
     * @param description - set new value of description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets lastLogin
     *
     * @return value of lastLogin field
     */
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    /**
     * Sets <code>Community</code> lastLogin value
     *
     * @param lastLogin - set new value of lastLogin
     */
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Gets user
     *
     * @return value of user field
     */
    public Person getUser() {
        return user;
    }

    /**
     * Sets <code>Community</code> user value
     *
     * @param user - set new value of user
     */
    public void setUser(Person user) {
        this.user = user;
    }


    public boolean equals(Object object) {
        if (object instanceof BaseEntity) {
            BaseEntity otherBaseEntity = (BaseEntity)object;
            return !(otherBaseEntity instanceof Person)
                    && this.getId().equals(otherBaseEntity.getId());
        } else {
            return false;
        }
    }

}
