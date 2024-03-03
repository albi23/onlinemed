package com.onlinemed.exception;

import java.util.List;

public record Violation(String errorUIkey, List<ViolationParam> violationParam) {

    public Violation(String errorUIkey, ViolationParam... violationParam){
        this(errorUIkey, List.of(violationParam));
    }
}
