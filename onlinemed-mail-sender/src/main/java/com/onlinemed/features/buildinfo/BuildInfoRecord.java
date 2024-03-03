package com.onlinemed.features.buildinfo;

import java.time.Instant;

 record BuildInfoRecord(
        String group,
        String artifact,
        String name,
        Instant buildTime,
        String version,
        String javaRuntimeVersion,
        String pid) {
}
