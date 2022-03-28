package com.onlinemed.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The class responsible for creating the doctor_info table and mapping data to the form
 * of DoctorInfo objects
 */
@Entity
@Table(name = "doctor_info")
public class DoctorInfo extends BaseObject {

    private String phoneNumber;
    private String doctorType;
    private String specialization;

    @OneToOne(mappedBy = "doctorInfo", fetch = FetchType.LAZY)
    private Person person;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "doctorInfo_id")
    private List<FacilityLocation> facilityLocations = new ArrayList<>();

    public DoctorInfo() {
    }

    public DoctorInfo(String phoneNumber, String doctorType, String specialization) {
        this.phoneNumber = phoneNumber;
        this.doctorType = doctorType;
        this.specialization = specialization;
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
     * Sets <code>DoctorInfo</code> phoneNumber value
     *
     * @param phoneNumber - set new value of phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets doctorType
     *
     * @return value of doctorType field
     */
    public String getDoctorType() {
        return doctorType;
    }

    /**
     * Sets <code>DoctorInfo</code> doctorType value
     *
     * @param doctorType - set new value of doctorType
     */
    public void setDoctorType(String doctorType) {
        this.doctorType = doctorType;
    }

    /**
     * Gets specialization
     *
     * @return value of specialization field
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Sets <code>DoctorInfo</code> specialization value
     *
     * @param specialization - set new value of specialization
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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
     * Sets <code>DoctorInfo</code> person value
     *
     * @param person - set new value of person
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Gets facilityLocations
     *
     * @return value of facilityLocations field
     */
    public List<FacilityLocation> getFacilityLocations() {
        return facilityLocations;
    }

    /**
     * Sets <code>DoctorInfo</code> facilityLocations value
     *
     * @param facilityLocations - set new value of facilityLocations
     */
    public void setFacilityLocations(List<FacilityLocation> facilityLocations) {
        this.facilityLocations = facilityLocations;
    }
}
