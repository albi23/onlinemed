package com.onlinemed.config.hibernate;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Properties;

import static com.onlinemed.config.BuildProfiles.PROD_PROFILE;

@Component
@Profile(PROD_PROFILE)
@PropertySources(value = {@PropertySource(name=PROD_PROFILE, value= "classpath:application-prod.properties")})
public class ProdHibernateProperties implements HibernatePropertiesConfig{

    private final Environment environment;

    public ProdHibernateProperties(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Properties configure() {
        final Properties prodProp = new Properties();
        final Properties source = (Properties) Objects.requireNonNull(((StandardEnvironment) environment)
                .getPropertySources().get(PROD_PROFILE)).getSource();
        source.keySet().stream()
                .filter(s -> ((String) s).startsWith("hibernate"))
                .forEach(x -> prodProp.put(x, source.get(x)));
        return prodProp;
    }
}
