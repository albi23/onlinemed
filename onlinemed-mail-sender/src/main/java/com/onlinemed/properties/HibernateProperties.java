package com.onlinemed.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.Map;
import java.util.Properties;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "hibernate")
public record HibernateProperties(
        String dialect,
        String showSql,
        String formatSql,
        String currentSessionContextClass,
        String orderInserts,
        String orderUpdates,
        Connection connection,
        Query query,
        Hbm2Ddl hbm2Ddl,
        Jdbc jdbc) {

    public Properties asProperties() {
        Properties properties = new Properties();
        properties.putAll(
                Map.ofEntries(
                        Map.entry("hibernate.dialect", dialect()),
                        Map.entry("hibernate.connection.driver_class", connection().driverClass()),
                        Map.entry("hibernate.hbm2ddl.auto", hbm2Ddl().auto()),
                        Map.entry("hibernate.show_sql", showSql()),
                        Map.entry("hibernate.format_sql", formatSql()),
                        Map.entry("hibernate.current_session_context_class", currentSessionContextClass()),

                        Map.entry("hibernate.connection.provider_disables_autocommit", connection().providerDisablesAutocommit()),
                        Map.entry("hibernate.jdbc.batch_size", jdbc().batchSize()),
                        Map.entry("hibernate.order_inserts", orderInserts()),
                        Map.entry("hibernate.order_updates", orderUpdates()),
                        Map.entry("hibernate.query.fail_on_pagination_over_collection_fetch", query().failOnPaginationOverCollectionFetch()),
                        Map.entry("hibernate.query.in_clause_parameter_padding", query().inClauseParameterPadding()),
                        Map.entry("hibernate.session.events.log.LOG_QUERIES_SLOWER_THAN_MS", Integer.toString(20))
                )
        );
        return properties;
    }
}






