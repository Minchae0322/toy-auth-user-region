package com.example.toyauth.app.core.enumuration;

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
