package com.onlinemed.properties;


public record Connection(
        String driverClass,
        String providerDisablesAutocommit
) {}