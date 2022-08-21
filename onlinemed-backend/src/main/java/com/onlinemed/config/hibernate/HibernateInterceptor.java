package com.onlinemed.config.hibernate;

import com.yannbriancon.interceptor.HibernateQueryInterceptor;
import com.yannbriancon.interceptor.HibernateQueryInterceptorProperties;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class HibernateInterceptor extends EmptyInterceptor {

    private final String ERROR_LVL = "INFO";
    private final HibernateQueryInterceptor hibernateQueryInterceptor;

    public HibernateInterceptor() {
        final HibernateQueryInterceptorProperties hibernateQueryInterceptorProperties = new HibernateQueryInterceptorProperties();
        hibernateQueryInterceptorProperties.setErrorLevel(ERROR_LVL);
        this.hibernateQueryInterceptor = new HibernateQueryInterceptor(hibernateQueryInterceptorProperties);
    }

    // required for create instance by spring reflection
    public static Interceptor init() {
        return new HibernateInterceptor();
    }

    @Override
    public String onPrepareStatement(String sql) {
        return hibernateQueryInterceptor.onPrepareStatement(sql);
    }


    @Override
    public void afterTransactionCompletion(Transaction tx) {
        hibernateQueryInterceptor.afterTransactionCompletion(tx);
    }

    @Override
    public Object getEntity(String entityName, Serializable id) {
        return hibernateQueryInterceptor.getEntity(entityName, id);
    }
}
