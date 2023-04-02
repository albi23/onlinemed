package com.onlinemed.model;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;
/**
 * The class responsible for creating the facility_location table and mapping data to the form
 * of FacilityLocation objects
 */
@Entity
@Table(name = "facility_location")
public class FacilityLocation extends BaseObject {

    private String facilityName;
    private String facilityAddress;

    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "field_key", length = 100)
    @Column(name = "price_value", length = 200)
    @CollectionTable(name = "price_values", joinColumns = @JoinColumn(name = "facility_location_id"))
    private Map<String, String> visitsPriceList = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private DoctorInfo doctorInfo;

    public FacilityLocation() {
    }

    public FacilityLocation(String facilityName, String facilityAddress) {
        this.facilityName = facilityName;
        this.facilityAddress = facilityAddress;
    }

    /**
     * Gets visitsPriceList
     *
     * @return value of visitsPriceList field
     */
    public Map<String, String> getVisitsPriceList() {
        return visitsPriceList;
    }

    /**
     * Sets <code>FacilityLocation</code> visitsPriceList value
     *
     * @param visitsPriceList - set new value of visitsPriceList
     */
    public void setVisitsPriceList(Map<String, String> visitsPriceList) {
        this.visitsPriceList = visitsPriceList;
    }

    /**
     * Gets facilityName
     *
     * @return value of facilityName field
     */
    public String getFacilityName() {
        return facilityName;
    }

    /**
     * Sets <code>FacilityLocation</code> facilityName value
     *
     * @param facilityName - set new value of facilityName
     */
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    /**
     * Gets facilityAddress
     *
     * @return value of facilityAddress field
     */
    public String getFacilityAddress() {
        return facilityAddress;
    }

    /**
     * Sets <code>FacilityLocation</code> facilityAddress value
     *
     * @param facilityAddress - set new value of facilityAddress
     */
    public void setFacilityAddress(String facilityAddress) {
        this.facilityAddress = facilityAddress;
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
     * Sets <code>FacilityLocation</code> doctorInfo value
     *
     * @param doctorInfo - set new value of doctorInfo
     */
    public void setDoctorInfo(DoctorInfo doctorInfo) {
        this.doctorInfo = doctorInfo;
    }
}
