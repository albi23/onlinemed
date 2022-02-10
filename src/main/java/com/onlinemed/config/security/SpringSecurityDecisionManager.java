package com.onlinemed.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.annotation.SecuredAnnotationSecurityMetadataSource;
import org.springframework.security.access.method.DelegatingMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.util.List;

/**
 * {@inheritDoc}
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityDecisionManager extends GlobalMethodSecurityConfiguration {

    /**
     * SpringSecurity requires prefix for each role name
     *
     * @see RoleVoter#getRolePrefix()
     **/
    @Override
    protected AccessDecisionManager accessDecisionManager() {
        AffirmativeBased manager = (AffirmativeBased) super.accessDecisionManager();
        for (AccessDecisionVoter<?> voter : manager.getDecisionVoters()) {
            if (voter instanceof RoleVoter)
                ((RoleVoter) voter).setRolePrefix("");
        }
        return manager;
    }

    /**
     * Override of creating security bean
     */
    @Override
    public MethodSecurityMetadataSource methodSecurityMetadataSource() {
        MethodSecurityMetadataSource metadataSource = super.methodSecurityMetadataSource();
        List<MethodSecurityMetadataSource> securitySources =
                ((DelegatingMethodSecurityMetadataSource) metadataSource).getMethodSecurityMetadataSources();
        for (int i = 0; i < securitySources.size(); i++) {
            if (securitySources.get(i) instanceof SecuredAnnotationSecurityMetadataSource) {
                securitySources.remove(i);
                securitySources.add(new SecuredAnnotationSecurityMetadataSourceWithInterfaces());
                break;
            }
        }
        return metadataSource;
    }
}