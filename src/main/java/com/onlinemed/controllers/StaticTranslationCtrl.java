package com.onlinemed.controllers;

import com.blueveery.core.ctrls.GetObjectCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.translations.StaticTranslation;
import com.onlinemed.servises.api.StaticTranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Locale;


/**
 * Class used to define methods that operate on objects of class StaticTranslation
 */
@PermitAll
@RestController
@RequestMapping("/api/static-translation")
@JsonScope(positive = true, scope = {StaticTranslation.class})
public class StaticTranslationCtrl implements GetObjectCtrl<StaticTranslation> {

    @Autowired
    StaticTranslationService staticTranslationService;

    @Override
    public BaseService<StaticTranslation> getService() {
        return this.staticTranslationService;
    }

    @RequestMapping(path = {"/{languageLocale}/{functionalityName}"}, method = {RequestMethod.GET}, produces = {"application/json"})
    @ResponseBody
    public List<StaticTranslation> getStaticTranslations(@PathVariable("languageLocale") Locale languageLocale,
                                                         @PathVariable("functionalityName") String functionalityName) {
        return staticTranslationService.findStaticTranslations(languageLocale, functionalityName);
    }
}
