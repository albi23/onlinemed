package com.onlinemed.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "languages")
public class Language extends BaseEntity {

    private Locale locale;

    private boolean isMaster;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "language")
    private Set<StaticTranslation> staticTranslations = new HashSet<>();

}