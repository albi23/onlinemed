package com.onlinemed.model;


import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The class responsible for creating the functionality table and mapping data to the form
 * of Functionality objects
 */
@Entity
@Table(name = "functionality")
public class Functionality extends BaseObject  {

    private String name;

    public Functionality() {
    }


    public Functionality(String name) {
        this.name = name;
    }

    /**
     * Gets name
     *
     * @return value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets <code>Functionality</code> name value
     *
     * @param name - set new value of name
     */
    public void setName(String name) {
        this.name = name;
    }
}