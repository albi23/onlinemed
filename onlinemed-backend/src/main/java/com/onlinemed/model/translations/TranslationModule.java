package com.onlinemed.model.translations;


import com.onlinemed.model.BaseObject;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * The class responsible for creating the translation_modules table and mapping data to the form
 * of TranslationModule objects
 */
@Entity
@Table(name = "translation_modules")
public class TranslationModule extends BaseObject {

    private String name;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "translationModule")
    private List<StaticTranslation> staticTranslations = new ArrayList<>();

    public TranslationModule() {
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
     * Sets <code>TranslationModule</code> name value
     *
     * @param name - set new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets staticTranslations
     *
     * @return value of staticTranslations field
     */
    public List<StaticTranslation> getStaticTranslations() {
        return staticTranslations;
    }

    /**
     * Sets <code>TranslationModule</code> staticTranslations value
     *
     * @param answers - set new value of staticTranslations
     */
    public void setStaticTranslations(List<StaticTranslation> answers) {
        this.staticTranslations = answers;
    }
}
