package com.onlinemed.features.mail;

import com.onlinemed.entities.StaticTranslation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Repository
interface StaticTranslationRepository extends CrudRepository<StaticTranslation, UUID> {

    @Transactional(readOnly = true)
    @Query(nativeQuery = true, name = "getTranslations")
    List<String> getTranslations(@Param("languageLocale") Locale languageLocale,
                                 @Param("moduleName") String moduleName,
                                 @Param("uiKeys") List<String> uiKeys);
}
