package com.onlinemed.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to define information about thrown validation exception
 */
public class Violation {

    /**
     * A key that defines the error content, which is the tranclation key
     */
    private String errorUIkey;

    /**
     * Optional arguments for the thrown error
     */
    private List<ViolationParam> paramList = new ArrayList<>();

    /**
     * Initializes a new instance of the Violation class.
     */
    public Violation() {
    }

    /**
     * Initializes a new instance of the Violation class.
     *
     * @param errorUIkey - translation key
     */
    public Violation(String errorUIkey) {
        this.errorUIkey = errorUIkey;
    }

    /**
     * Initializes a new instance of the Violation class.
     *
     * @param errorUIkey     - translation key
     * @param violationParam - optional args
     */
    public Violation(String errorUIkey, ViolationParam... violationParam) {
        this.errorUIkey = errorUIkey;
        this.paramList = List.of(violationParam);

    }

    /**
     * Gets errorUIkey
     *
     * @return value of errorUIkey
     */
    public String getErrorUIkey() {
        return errorUIkey;
    }

    /**
     * Gets paramList
     *
     * @return value of paramList
     */
    public List<ViolationParam> getParamList() {
        return paramList;
    }

    /**
     * Sets <code>Violation</code> paramList value
     *
     * @param paramList - set new value of paramList
     */
    public void setParamList(List<ViolationParam> paramList) {
        this.paramList = paramList;
    }

    /**
     * Sets <code>Violation</code> errorUIkey value
     *
     * @param errorUIkey - set new value of errorUIkey
     */
    public void setErrorUIkey(String errorUIkey) {
        this.errorUIkey = errorUIkey;
    }

    /**
     * Sets <code>Violation</code> paramList value
     *
     * @param violationParam - set new value of paramList
     */
    public void setParam(ViolationParam... violationParam) {
        this.paramList = List.of(violationParam);
    }


}
