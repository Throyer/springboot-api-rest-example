package com.github.throyer.common.springboot.domain.models.shared;

import lombok.Data;

@Data
public class Toast {

    private String message;
    private String type;

    private Toast(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public static Toast of(String message, Type type) {
        return new Toast(message, type.name);
    }
}
