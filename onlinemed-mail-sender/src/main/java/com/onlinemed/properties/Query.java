package com.onlinemed.properties;

public record Query(
        String failOnPaginationOverCollectionFetch,
        String inClauseParameterPadding
) {}