package com.onlinemed.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * The class responsible for creating the person table and mapping data to the form
 * of Person objects
 */
@Entity
@Table(name = "person", indexes = {@Index(name = "person_index", columnList = "userName")})
public class Person extends BaseObject {

    private String name;
    private String surname;
    private String userName;
    private String email;
    private String phoneNumber;

    @Column(columnDefinition = "varchar(5) default 'en_GB'") // Locale.UK
    private Locale defaultLanguage = Locale.UK;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Community community = new Community();

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "security_id", referencedColumnName = "id")
    private Security security;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "person_x_role",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<Role> roles = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "person_x_doctor_info",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "doctor_info_id")}
    )
    private DoctorInfo doctorInfo;


    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CalendarEvent> calendarEvents = new ArrayList<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    public Person() {
    }

    public Person(UUID id, String name, String surname, String userName, String email, String phoneNumber) {
        this.setId(id);
        this.name = name;
        this.surname = surname;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
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
     * Sets <code>Person</code> name value
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
     * Sets <code>Person</code> surname value
     *
     * @param surname - set new value of surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets userName
     *
     * @return value of userName field
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets <code>Person</code> userName value
     *
     * @param userName - set new value of userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets email
     *
     * @return value of email field
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets <code>Person</code> email value
     *
     * @param email - set new value of email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets phoneNumber
     *
     * @return value of phoneNumber field
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets <code>Person</code> phoneNumber value
     *
     * @param phoneNumber - set new value of phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets security
     *
     * @return value of security field
     */
    public Security getSecurity() {
        return security;
    }

    /**
     * Sets <code>Person</code> security value
     *
     * @param security - set new value of security
     */
    public void setSecurity(Security security) {
        this.security = security;
    }

    /**
     * Gets roles
     *
     * @return value of roles field
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Sets <code>Person</code> roles value
     *
     * @param roles - set new value of roles
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * Gets defaultLanguage
     *
     * @return value of defaultLanguage field
     */
    public Locale getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Sets <code>Person</code> defaultLanguage value
     *
     * @param defaultLanguage - set new value of defaultLanguage
     */
    public void setDefaultLanguage(Locale defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    /**
     * Gets community
     *
     * @return value of community field
     */
    public Community getCommunity() {
        return community;
    }

    /**
     * Sets <code>Person</code> community value
     *
     * @param community - set new value of community
     */
    public void setCommunity(Community community) {
        this.community = community;
    }

    /**
     * Gets doctorInfo
     *
     * @return value of doctorInfo field
     */
    public DoctorInfo getDoctorInfo() {
        return doctorInfo;
    }

    /**
     * Sets <code>Person</code> doctorInfo value
     *
     * @param doctorInfo - set new value of doctorInfo
     */
    public void setDoctorInfo(DoctorInfo doctorInfo) {
        this.doctorInfo = doctorInfo;
    }

    /**
     * Gets calendarEvents
     *
     * @return value of calendarEvents field
     */
    public List<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    /**
     * Sets <code>Person</code> calendarEvents value
     *
     * @param calendarEvents - set new value of calendarEvents
     */
    public void setCalendarEvents(List<CalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    /**
     * Gets notifications
     *
     * @return value of notifications field
     */
    public List<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Sets <code>Person</code> notifications value
     *
     * @param notifications - set new value of notifications
     */
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
