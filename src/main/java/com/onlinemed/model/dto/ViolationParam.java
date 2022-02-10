package com.onlinemed.model.dto;

public class ViolationParam {

    private String name;
    private Object value;

    public ViolationParam() {
    }

    public ViolationParam(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets name
     *
     * @return value of name field
     */
    public String getName() {
        return name;
    }

    /**
     * Gets value
     *
     * @return value of value field
     */
    public Object getValue() {
        return value;
    }
}
