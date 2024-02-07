package com.onlinemed.dto;

import java.time.Instant;

public record BuildInfoRecord(
        String group,
        String artifact,
        String name,
        Instant buildTime,
        String version,
        String javaRuntimeVersion,
        String pid) {
}
