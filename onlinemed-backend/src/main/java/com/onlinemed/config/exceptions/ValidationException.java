package com.onlinemed.config.exceptions;

import com.onlinemed.model.dto.Violation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Base exception of all Bean Validation "unexpected" problems.
 */
public class ValidationException extends javax.validation.ValidationException {

    /**
     * List containing <code>Violation</code> objects
     */
    private List<Violation> violationList = new ArrayList<>();

    /**
     * Creates a ValidationException class object with the given list
     *
     * @param violationList -  list with object <code>Violation</code>
     */
    public ValidationException(List<Violation> violationList) {
        this.violationList = violationList;
    }

    public ValidationException(Violation violation, Violation... violations) {
        ArrayList<Violation> tmp = new ArrayList<>(violations.length + 1);
        tmp.add(violation);
        tmp.addAll(Arrays.asList(violations));
        this.violationList = tmp;
    }

    /**
     * Creates a ValidationException class object with the given  <code>Violation</code> object
     *
     * @param violation -  <code>Violation</code> object
     */
    public ValidationException(Violation violation) {
        violationList.add(violation);
    }

    /**
     * @return - List containing <code>Violation</code> objects
     */
    public List<Violation> getViolationList() {
        return violationList;
    }
}
