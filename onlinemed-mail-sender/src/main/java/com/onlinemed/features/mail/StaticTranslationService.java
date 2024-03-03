package com.onlinemed.features.mail;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class StaticTranslationService {

    private final StaticTranslationRepository staticTranslationRepository;

    StaticTranslationService(StaticTranslationRepository staticTranslationRepository) {
        this.staticTranslationRepository = staticTranslationRepository;
    }

    public List<String> getTranslations(Locale languageLocale,
                                 String moduleName,
                                 List<String> uiKeys) {
        return staticTranslationRepository.getTranslations(languageLocale, moduleName, uiKeys);
    }
}
