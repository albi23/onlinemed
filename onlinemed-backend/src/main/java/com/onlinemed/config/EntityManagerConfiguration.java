package com.onlinemed.config;

import com.onlinemed.config.hibernate.HibernatePropertiesConfig;
import com.onlinemed.config.micrometer.MicrometerListener;
import com.onlinemed.model.BaseObject;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static com.onlinemed.config.BuildProfiles.DEV_PROFILE;
import static com.onlinemed.config.BuildProfiles.TEST_PROFILE;

/**
 * The class responsible for the configuration of the entity manager
 * based on the values  in the application.*.properties file
 */
@Configuration
public class EntityManagerConfiguration {

    private static final String modelPackagePath = BaseObject.class.getPackageName();

    private final HibernatePropertiesConfig hibernateProp;

    public EntityManagerConfiguration(HibernatePropertiesConfig hibernateProp) {
        this.hibernateProp = hibernateProp;
    }

    @Bean
    public Properties hibernateProperties() {
        return this.hibernateProp.configure();
    }

    @Bean
    @Primary
    public EntityManagerFactory entityManagerFactory(DataSource dataSource, Properties hibernateProperties) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        return getEntityManagerFactory(hibernateProperties, em);
    }

    private EntityManagerFactory getEntityManagerFactory(Properties hibernateProperties, LocalContainerEntityManagerFactoryBean em) {
        em.setPackagesToScan(modelPackagePath);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(hibernateProperties);
        em.setPersistenceUnitName("om-unit");
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        em.afterPropertiesSet();
        return em.getObject();
    }


    @Bean
    @Profile({TEST_PROFILE})
    public EntityManagerFactory entityManagerFactoryWithProxy(DataSource dataSource, Properties hibernateProperties) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(proxyDatasource(dataSource));
        return getEntityManagerFactory(hibernateProperties, em);
    }

    public ProxyDataSource proxyDatasource(DataSource dataSource) {
        var listener = new MicrometerListener();
//        listener.setLogLevel(SLF4JLogLevel.TRACE);

        return ProxyDataSourceBuilder.create(dataSource)
                .name("DataSource")
                .countQuery()
                .listener(listener)
                .build();
    }
}
