package com.github.throyer.common.springboot.domain.shared;

public enum Type {

    PRIMARY("bg-primary text-light"),
    SECONDARY("bg-secondary text-light"),
    SUCCESS("bg-success text-light"),
    INFO("bg-info text-light"),
    WARNING("bg-warning"),
    DANGER("bg-danger text-light"),
    LIGHT("bg-light"),
    DARK("bg-dark text-light");

    public String name;

    Type(String name) {
        this.name = name;
    }
}
