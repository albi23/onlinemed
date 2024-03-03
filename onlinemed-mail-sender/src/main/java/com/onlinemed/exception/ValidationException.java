package com.onlinemed.exception;

import java.util.List;

public final class ValidationException extends RuntimeException {

    private final List<Violation> violationList;

    public ValidationException(Violation violation) {
        this.violationList = List.of(violation);
    }

    public List<Violation> getViolationList() {
        return violationList;
    }
}
