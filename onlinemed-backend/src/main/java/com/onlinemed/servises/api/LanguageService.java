package com.onlinemed.servises.api;


import com.onlinemed.model.translations.Language;

public interface LanguageService extends BaseObjectService<Language> {

    Language findMasterLanguage();

}
