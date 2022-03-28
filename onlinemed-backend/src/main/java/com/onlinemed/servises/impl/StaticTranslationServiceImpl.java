package com.onlinemed.servises.impl;


import com.onlinemed.model.translations.*;
import com.onlinemed.servises.api.LanguageService;
import com.onlinemed.servises.api.StaticTranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class StaticTranslationServiceImpl extends BaseObjectServiceImpl<StaticTranslation> implements StaticTranslationService {


    @Autowired
    LanguageService languageService;

    @Override
    public List<StaticTranslation> findStaticTranslations(Locale languageLocale, String moduleName) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<StaticTranslation> criteriaQuery = criteriaBuilder.createQuery(StaticTranslation.class);
        Root<StaticTranslation> staticTranslationRoot = criteriaQuery.from(StaticTranslation.class);

        Join<StaticTranslation, TranslationModule> moduleJoin = staticTranslationRoot.join(StaticTranslation_.translationModule);
        Predicate functionalityPredicate = criteriaBuilder.equal(moduleJoin.get(TranslationModule_.name), moduleName);

        Join<StaticTranslation, Language> languageJoin = staticTranslationRoot.join(StaticTranslation_.language);
        Predicate localePredicate = criteriaBuilder.equal(languageJoin.get(Language_.locale), languageLocale);

        final Predicate and = criteriaBuilder.and(functionalityPredicate, localePredicate);
        TypedQuery<StaticTranslation> staticTranslationTypedQuery = getEntityManager().createQuery(criteriaQuery.where(and));
        return staticTranslationTypedQuery.getResultList();
    }

    @Override
    public List<StaticTranslation> getStaticTranslation(Locale languageLocale, String moduleName, String... uiKey) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<StaticTranslation> criteriaQuery = criteriaBuilder.createQuery(StaticTranslation.class);
        Root<StaticTranslation> staticTranslationRoot = criteriaQuery.from(StaticTranslation.class);

        Join<StaticTranslation, TranslationModule> moduleJoin = staticTranslationRoot.join(StaticTranslation_.translationModule);
        Predicate functionalityPredicate = criteriaBuilder.equal(moduleJoin.get(TranslationModule_.name), moduleName);

        Join<StaticTranslation, Language> languageJoin = staticTranslationRoot.join(StaticTranslation_.language);
        Predicate localePredicate = criteriaBuilder.equal(languageJoin.get(Language_.locale), languageLocale);
        final Predicate[] predicates = new Predicate[]{
                criteriaBuilder.and(functionalityPredicate, localePredicate),
                staticTranslationRoot.get(StaticTranslation_.uiKey).in(Arrays.asList(uiKey))
        };
        TypedQuery<StaticTranslation> staticTranslationTypedQuery = getEntityManager().createQuery(criteriaQuery.where(predicates));
        return staticTranslationTypedQuery.getResultList();
    }


}
