package com.onlinemed.model;

import com.blueveery.core.model.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The main class in the system. Inherits from BaseEntity, which is the basis for
 * the operation of libraries related to data serialization
 */
@MappedSuperclass
@AttributeOverrides(value = @AttributeOverride(name = "id", column = @Column(columnDefinition = "uuid")))
public class BaseObject extends BaseEntity implements Serializable {

    @Version
    private int version;

    @Column(columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    protected LocalDateTime timestamp = LocalDateTime.now();

    public BaseObject() {
    }

    /**
     * Gets timestamp
     *
     * @return value of timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets <code>BaseObject</code> timestamp value
     *
     * @param timestamp - set new value of timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Updates timestamp to current
     */
    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        timestamp = LocalDateTime.now();
    }

    /**
     * The method responsible for touching the object - attaching it to json-scope
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored"}) // hide warning
    public final void touchObject() {
        this.getVersion();
    }

    /**
     * Gets version
     *
     * @return value of version field
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets <code>BaseObject</code> version value
     *
     * @param version - set new value of version
     */
    public void setVersion(int version) {
        this.version = version;
    }
}
