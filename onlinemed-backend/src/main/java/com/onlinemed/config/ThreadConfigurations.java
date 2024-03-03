package com.onlinemed.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * The class responsible for providing the multithreading configuration
 */
@Configuration
@EnableAsync
public class ThreadConfigurations {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadConfigurations.class);

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        LOGGER.debug("Creating Async Task Executor");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cores = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(cores);
        int maxPoolSize = cores * 3;
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity((int)(maxPoolSize * 2.5));
        executor.setPrestartAllCoreThreads(true);
        executor.setThreadNamePrefix("Thread-");
        executor.initialize();
        return executor;
    }
}