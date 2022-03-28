package com.onlinemed.model.translations;


import com.onlinemed.model.BaseObject;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * The class responsible for creating the languages table and mapping data to the form
 * of Language objects
 */
@Entity
@Table(name = "languages")
public class Language extends BaseObject {

    private Locale locale;
    private boolean isMaster;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "language")
    private List<StaticTranslation> staticTranslations = new ArrayList<>();

    public Language() {
    }

    public List<StaticTranslation> getStaticTranslations() {
        return staticTranslations;
    }

    public void setStaticTranslations(List<StaticTranslation> staticTranslations) {
        this.staticTranslations = staticTranslations;
    }

    /**
     * Gets locale
     *
     * @return value of locale field
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets <code>Language</code> locale value
     *
     * @param locale - set new value of locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Sets <code>Language</code> isMaster value
     *
     * @param isMaster - set new value of isMaster
     */
    public void setIsMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }

    /**
     * Gets isMaster
     *
     * @return value of isMaster field
     */
    public boolean getLocaleMaster() {
        return isMaster;
    }
}