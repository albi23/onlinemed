package com.onlinemed.servises.impl.login;

import com.onlinemed.model.Functionality;
import org.springframework.security.core.GrantedAuthority;

public class FunctionalityGrantedAuthority implements GrantedAuthority {

    private final Functionality functionality;

    /**
     * @param functionality Functionality object
     */
    public FunctionalityGrantedAuthority(Functionality functionality) {
        this.functionality = functionality;
    }

    /**
     * @return String representation of functionality
     */
    @Override
    public String getAuthority() {
        return functionality.getName();
    }
}
