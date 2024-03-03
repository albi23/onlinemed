package com.onlinemed.cfg;

import com.onlinemed.entities.BaseEntity;
import com.onlinemed.properties.HibernateProperties;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/**
 * The class responsible for the configuration of the entity manager
 * based on the values  in the application.*.properties file
 */
@Configuration
public class EntityManagerConfiguration {

    private final HibernateProperties hibernateProp;

    public EntityManagerConfiguration(HibernateProperties hibernateProp) {
        this.hibernateProp = hibernateProp;
    }

    @Bean
    @Primary
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        return getEntityManagerFactory(em);
    }

    private EntityManagerFactory getEntityManagerFactory( LocalContainerEntityManagerFactoryBean em) {
        em.setPackagesToScan(BaseEntity.class.getPackageName());
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(hibernateProp.asProperties());
        em.setPersistenceUnitName("om-unit-mail");
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        em.afterPropertiesSet();
        return em.getObject();
    }


}
