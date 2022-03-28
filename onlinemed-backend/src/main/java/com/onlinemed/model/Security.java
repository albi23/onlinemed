package com.onlinemed.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The class responsible for creating the security table and mapping data to the form
 * of Security objects
 */
@Entity
@Table(name = "security")
public class Security extends BaseObject {

    private String hash;
    private String token;
    private String securityToken;

    @OneToOne(mappedBy = "security")
    private Person person;

    public Security() {
    }

    public Security(Person person, String hash) {
        this.hash = hash;
        this.person = person;
    }

    /**
     * Gets hash
     *
     * @return value of hash field
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets <code>Security</code> hash value
     *
     * @param hash - set new value of hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Gets token
     *
     * @return value of token field
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets <code>Security</code> token value
     *
     * @param token - set new value of token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets securityToken
     *
     * @return value of securityToken field
     */
    public String getSecurityToken() {
        return securityToken;
    }

    /**
     * Sets <code>Security</code> securityToken value
     *
     * @param securityToken - set new value of securityToken
     */
    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    /**
     * Gets person
     *
     * @return value of person field
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Sets <code>Security</code> person value
     *
     * @param person - set new value of person
     */
    public void setPerson(Person person) {
        this.person = person;
    }
}
