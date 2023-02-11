package com.onlinemed.servises.impl.login;

import com.onlinemed.model.Functionality;
import org.springframework.security.core.GrantedAuthority;

public record FunctionalityGrantedAuthority(Functionality functionality) implements GrantedAuthority {

    /**
     * @param functionality Functionality object
     */
    public FunctionalityGrantedAuthority {
    }

    /**
     * @return String representation of functionality
     */
    @Override
    public String getAuthority() {
        return functionality.getName();
    }
}
