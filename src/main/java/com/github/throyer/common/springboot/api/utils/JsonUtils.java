package com.github.throyer.common.springboot.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private JsonUtils() { }
    
    public static <T> String toJson(final T object) {
        final var writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            return "";
        }
    }
}