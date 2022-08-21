package com.onlinemed.config.hibernate;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static com.onlinemed.config.BuildProfiles.DEV_PROFILE;

@Component
@Profile(DEV_PROFILE)
@PropertySources(value = {@PropertySource(name = DEV_PROFILE, value = "classpath:application.properties")})
public class DevHibernateProperties implements HibernatePropertiesConfig {

    private final Map<String, String> CUSTOM_DEV_PROP = Map.of(
            "hibernate.session_factory.interceptor", HibernateInterceptor.class.getName()
    );
    private final Environment environment;

    public DevHibernateProperties(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Properties configure() {
        final Properties devProperties = new Properties();
        final Properties sourceProp = (Properties) Objects.requireNonNull(((StandardEnvironment) environment)
                .getPropertySources().get(DEV_PROFILE)).getSource();
        sourceProp.keySet().stream()
                .filter(s -> ((String) s).startsWith("hibernate"))
                .forEach(x -> devProperties.put(x, sourceProp.get(x)));
        devProperties.putAll(CUSTOM_DEV_PROP);
        return devProperties;
    }
}
