package com.example.toyauth.app.common.enumuration;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role {

    ADMIN("ADMIN"),
    USER("USER");


    Role(String role) {
        this.role = role;
    }

    public String getValue() {
        return this.role;
    }

    private final String role;
}
