package com.onlinemed.model.translations;



import com.onlinemed.model.BaseObject;
import com.onlinemed.model.enums.TranslationStatus;

import javax.persistence.*;

/**
 * The class responsible for creating the static_translations table and mapping data to the form
 * of StaticTranslation objects
 */
@Table(indexes = {@Index(name = "uiKey_index", columnList = "uiKey")})
@Entity(name = "static_translations")
public class StaticTranslation extends BaseObject {

    private String name;
    private String uiKey;
    @Enumerated(EnumType.STRING)
    @Column(length = 7)
    private TranslationStatus status;

    @JoinColumn(name = "language")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Language language;

    @JoinColumn(name = "translation_module")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private TranslationModule translationModule;

    /**
     * Gets name
     *
     * @return value of name field
     */
    public String getName() {
        return name;
    }

    /**
     * Sets <code>StaticTranslation</code> name value
     *
     * @param name - set new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets status
     *
     * @return value of status field
     */
    public TranslationStatus getStatus() {
        return status;
    }

    /**
     * Sets <code>StaticTranslation</code> status value
     *
     * @param status - set new value of status
     */
    public void setStatus(TranslationStatus status) {
        this.status = status;
    }

    /**
     * Gets uiKey
     *
     * @return value of uiKey field
     */
    public String getUiKey() {
        return uiKey;
    }

    /**
     * Sets <code>StaticTranslation</code> uiKey value
     *
     * @param uiKey - set new value of uiKey
     */
    public void setUiKey(String uiKey) {
        this.uiKey = uiKey;
    }

    /**
     * Gets language
     *
     * @return value of language field
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets <code>StaticTranslation</code> language value
     *
     * @param language - set new value of language
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Gets translationModule
     *
     * @return value of translationModule field
     */
    public TranslationModule getTranslationModule() {
        return translationModule;
    }

    /**
     * Sets <code>StaticTranslation</code> translationModule value
     *
     * @param translationModule - set new value of translationModule
     */
    public void setTranslationModule(TranslationModule translationModule) {
        this.translationModule = translationModule;
    }
}
