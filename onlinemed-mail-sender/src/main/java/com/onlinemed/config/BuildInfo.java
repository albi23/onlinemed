package com.onlinemed.config;

import com.onlinemed.dto.BuildInfoRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Configuration
public class BuildInfo {

    private final BuildProperties buildProperties;

    public BuildInfo(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public BuildInfoRecord collectBuildInfo(){
        var buildInfo = new BuildInfoRecord(
                buildProperties.getGroup(),
                buildProperties.getArtifact(),
                buildProperties.getName(),
                Optional.ofNullable(buildProperties.getTime()).orElse(Instant.now()),
                buildProperties.getVersion(),
                System.getProperty("java.runtime.version"),
                System.getProperty("PID")
        );
        log.info("Build info {}", buildInfo);
        return buildInfo;
    }
}
