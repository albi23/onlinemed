package com.onlinemed.entities;


import com.onlinemed.enums.TranslationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(indexes = {@Index(name = "uiKey_index", columnList = "uiKey")})
@Entity(name = "static_translations")
@NamedNativeQueries(value = {
        @NamedNativeQuery(name = "getTranslations",
                resultClass = String.class,
                query = """
                   SELECT ST.name
                   FROM static_translations st
                            LEFT JOIN translation_modules tm
                                      ON st.translation_module = tm.id
                            LEFT JOIN languages lang
                                ON st.language = lang.id
                   WHERE lang.locale = :languageLocale and tm.name = :moduleName and st.uikey IN :uiKeys
                   """)
})
/*@SqlResultSetMappings(value = {
        @SqlResultSetMapping(name = "", classes = {})
})*/
public class StaticTranslation extends BaseEntity {

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
}