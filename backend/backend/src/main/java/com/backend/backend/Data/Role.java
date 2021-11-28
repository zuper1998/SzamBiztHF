package com.backend.backend.Data;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Role {
    @Id
    @GeneratedValue
    @Type(type="uuid-char")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleEnum name;

    public Role() {

    }

    public Role(RoleEnum name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }
}
