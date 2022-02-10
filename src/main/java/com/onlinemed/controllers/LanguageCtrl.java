package com.onlinemed.controllers;

import com.blueveery.core.ctrls.FindAllCtrl;
import com.blueveery.core.ctrls.GetObjectCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.translations.Language;
import com.onlinemed.servises.api.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Class used to define methods that operate on objects of class Language
 */
@RestController
@RequestMapping("/api/language")
@JsonScope(positive = true, scope = {Language.class})
public class LanguageCtrl implements FindAllCtrl<Language>, GetObjectCtrl<Language> {

    @Autowired
    private LanguageService languageService;

    @Override
    public BaseService<Language> getService() {
        return languageService;
    }
}
