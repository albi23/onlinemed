package com.onlinemed.config;

import com.onlinemed.model.BaseObject;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

/**
 * The class responsible for the configuration of the entity manager
 * based on the values  in the application.*.properties file
 */
@Configuration
@PropertySources(value = {@PropertySource("classpath:application.properties")})
public class EntityManagerConfiguration {

    private static final String modelPackagePath = BaseObject.class.getPackageName();

    @Autowired
    private Environment environment;

    @Bean
    public Properties hibernateProperties() {
        final Properties properties = new Properties();
        Properties source = (Properties) (Objects.requireNonNull(((StandardEnvironment) environment).getPropertySources()
                .get("class path resource [application.properties]"))).getSource();
        source.keySet().stream()
                .filter(s -> ((String) s).startsWith("hibernate"))
                .forEach(x -> properties.put(x, source.get(x)));
        return properties;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource, Properties hibernateProperties) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(modelPackagePath);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(hibernateProperties);
        em.setPersistenceUnitName("om-unit");
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        em.afterPropertiesSet();
        return em.getObject();
    }
}
