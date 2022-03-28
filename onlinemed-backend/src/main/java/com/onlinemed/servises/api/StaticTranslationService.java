package com.onlinemed.servises.api;


import com.onlinemed.model.translations.StaticTranslation;

import java.util.List;
import java.util.Locale;

public interface StaticTranslationService extends BaseObjectService<StaticTranslation> {

    List<StaticTranslation> findStaticTranslations(Locale languageLocale, String moduleName);

    List<StaticTranslation> getStaticTranslation(Locale languageLocale, String moduleName, String... uiKey);
}
