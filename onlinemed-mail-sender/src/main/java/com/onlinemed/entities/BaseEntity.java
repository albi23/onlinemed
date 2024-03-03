package com.onlinemed.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id ;

    @Version
    private int version;

    @Column(columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    protected LocalDateTime timestamp = LocalDateTime.now();

    public BaseEntity() {
        this.id = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that instanceof BaseEntity thatEntity &&
                Objects.equals(id, thatEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        timestamp = LocalDateTime.now();
    }
}
