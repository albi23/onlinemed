package com.onlinemed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinemed.model.enums.NotificationType;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.UUID;

/**
 * The class responsible for creating the notification table and mapping data to the form
 * of Notification objects
 */
@Entity
@Table(name = "notification")
public class Notification extends BaseObject {

    private NotificationType notificationType = NotificationType.EMAIL;
    private UUID senderId;
    private String name;
    private String surname;

    @JsonIgnore
    @Column(name = "visit_id", insertable = false, updatable = false)
    private UUID visit_id;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "visit_id", referencedColumnName = "id")
    @Nullable
    private Visit visit;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public Notification() {
    }

    public Notification(UUID senderId, Person person, String name, String surname) {
        this.senderId = senderId;
        this.person = person;
        this.name = name;
        this.surname = surname;
    }

    /**
     * Gets notificationType
     *
     * @return value of notificationType field
     */
    public NotificationType getNotificationType() {
        return notificationType;
    }

    /**
     * Sets <code>Notification</code> notificationType value
     *
     * @param notificationType - set new value of notificationType
     */
    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    /**
     * Gets visit
     *
     * @return value of visit field
     */
    @Nullable
    public Visit getVisit() {
        return visit;
    }

    /**
     * Sets <code>Notification</code> visit value
     *
     * @param visit - set new value of visit
     */
    public void setVisit(@Nullable Visit visit) {
        this.visit = visit;
    }

    /**
     * Gets senderId
     *
     * @return value of senderId field
     */
    public UUID getSenderId() {
        return senderId;
    }

    /**
     * Sets <code>Notification</code> senderId value
     *
     * @param senderId - set new value of senderId
     */
    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    /**
     * Gets person
     *
     * @return value of person field
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Sets <code>Notification</code> person value
     *
     * @param person - set new value of person
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Gets name
     *
     * @return value of name field
     */
    public String getName() {
        return name;
    }

    /**
     * Sets <code>Notification</code> name value
     *
     * @param name - set new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets surname
     *
     * @return value of surname field
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets <code>Notification</code> surname value
     *
     * @param surname - set new value of surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
}
