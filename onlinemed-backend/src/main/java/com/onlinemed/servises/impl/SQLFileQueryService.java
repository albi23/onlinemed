package com.onlinemed.servises.impl;

import com.onlinemed.functionalUtils.FunctionalUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.stream.Stream;

@Service
public record SQLFileQueryService(DataSource dataSource) {

    public void executeScriptsSql(final String... scripts) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.setSeparator(ScriptUtils.EOF_STATEMENT_SEPARATOR);
        final ClassLoader classLoader = this.getClass().getClassLoader();
        Stream.of(scripts).map(classLoader::getResourceAsStream).forEach(FunctionalUtils.throwingConsumerWrapper(s ->
                resourceDatabasePopulator.addScript(new InputStreamResource(s))));
        try {
            resourceDatabasePopulator.execute(dataSource);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }
}
