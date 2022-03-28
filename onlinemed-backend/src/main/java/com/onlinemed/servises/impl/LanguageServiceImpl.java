package com.onlinemed.servises.impl;


import com.onlinemed.model.translations.Language;
import com.onlinemed.model.translations.Language_;
import com.onlinemed.servises.api.LanguageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Locale;

@Service
public class LanguageServiceImpl extends BaseObjectServiceImpl<Language> implements LanguageService {

    /**
     * Cached master language for translations
     */
    private Language masterLanguage;

    @Value("${default.language}")
    private String stringLocale;

    @Override
    protected void validateBeforeUpdate(Language entity) {
        if (!entity.getLocale().toString().equals(stringLocale) && entity.getLocaleMaster()){
            throw new IllegalStateException("Only 1 main language is allowed");
        }
    }


    @Override
    @Transactional
    public Language findMasterLanguage() {

        if (masterLanguage != null) return masterLanguage;

        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Language> criteriaQuery = criteriaBuilder.createQuery(Language.class);
        Root<Language> rootEntity = criteriaQuery.from(Language.class);
        String[] lang = stringLocale.split("_");
        Predicate masterLanguagePredicate = criteriaBuilder.equal(rootEntity.get(Language_.locale), new Locale(lang[0], lang[1]));
        CriteriaQuery<Language> selectQuery = criteriaQuery.select(rootEntity).where(masterLanguagePredicate);
        this.masterLanguage = this.getEntityManager().createQuery(selectQuery).getSingleResult();
        return this.masterLanguage;
    }

}
