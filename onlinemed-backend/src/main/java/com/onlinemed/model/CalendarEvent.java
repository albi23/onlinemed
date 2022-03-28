package com.onlinemed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The class responsible for creating the calendar_event table and mapping data to the form
 * of CalendarEvent objects
 */
@Entity
@Table(name = "calendar_event")
@AttributeOverrides(value = @AttributeOverride(name = "timestamp", column = @Column(name = "start_date")))
public class CalendarEvent extends BaseObject {

    private LocalDateTime endDate;
    private String title;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @JsonIgnore
    @Column(name = "person_id", insertable = false, updatable = false)
    private UUID personId;

    public CalendarEvent() {
    }

    public CalendarEvent(LocalDateTime startDate, LocalDateTime endDate, String title, Person person) {
        this.timestamp = startDate;
        this.endDate = endDate;
        this.title = title;
        this.person = person;
    }

    /**
     * Do not update after changes
     */
    public void updateTimestamp() {
    }


    /**
     * Gets endDate
     *
     * @return value of endDate field
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Sets <code>CalendarEvent</code> endDate value
     *
     * @param endDate - set new value of endDate
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets title
     *
     * @return value of title field
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets <code>CalendarEvent</code> title value
     *
     * @param title - set new value of title
     */
    public void setTitle(String title) {
        this.title = title;
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
     * Sets <code>CalendarEvent</code> person value
     *
     * @param person - set new value of person
     */
    public void setPerson(Person person) {
        this.person = person;
    }
}
