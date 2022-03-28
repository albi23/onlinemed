package com.onlinemed.config.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * {@inheritDoc}
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityWebApplicationInitializer() {
        super(WebSecurityConfiguration.class);
    }

}
