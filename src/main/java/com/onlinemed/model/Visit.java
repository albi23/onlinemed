package com.onlinemed.model;

import com.onlinemed.model.enums.VisitState;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * The class responsible for creating the visit table and mapping data to the form
 * of Community Visit
 */
@Entity
@Table(name = "visit")
public class Visit extends BaseObject {

    private String visitType;
    private String localisation;
    private String optionalMessage;
    private VisitState visitState = VisitState.PENDING;
    private LocalDateTime visitDate;

    public Visit() {
    }

    public Visit(String visitType, String localisation, String optionalMessage, LocalDateTime visitDate) {
        this.visitType = visitType;
        this.localisation = localisation;
        this.optionalMessage = optionalMessage;
        this.visitDate = visitDate;
    }

    public Visit(String visitType, String localisation, LocalDateTime visitDate) {
        this.visitType = visitType;
        this.localisation = localisation;
        this.visitDate = visitDate;
    }

    /**
     * Gets visitType
     *
     * @return value of visitType field
     */
    public String getVisitType() {
        return visitType;
    }

    /**
     * Sets <code>Visit</code> visitType value
     *
     * @param visitType - set new value of visitType
     */
    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    /**
     * Gets localisation
     *
     * @return value of localisation field
     */
    public String getLocalisation() {
        return localisation;
    }

    /**
     * Sets <code>Visit</code> localisation value
     *
     * @param localisation - set new value of localisation
     */
    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    /**
     * Gets visitDate
     *
     * @return value of visitDate field
     */
    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    /**
     * Sets <code>Visit</code> visitDate value
     *
     * @param visitDate - set new value of visitDate
     */
    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }

    /**
     * Gets optionalMessage
     *
     * @return value of optionalMessage field
     */
    public String getOptionalMessage() {
        return optionalMessage;
    }

    /**
     * Sets <code>Visit</code> optionalMessage value
     *
     * @param optionalMessage - set new value of optionalMessage
     */
    public void setOptionalMessage(String optionalMessage) {
        this.optionalMessage = optionalMessage;
    }

    /**
     * Gets visitState
     *
     * @return value of visitState field
     */
    public VisitState getVisitState() {
        return visitState;
    }

    /**
     * Sets <code>Visit</code> visitState value
     *
     * @param visitState - set new value of visitState
     */
    public void setVisitState(VisitState visitState) {
        this.visitState = visitState;
    }
}
