package com.github.throyer.common.springboot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonUtils {

    private JsonUtils() { }

    private static final ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
    
    public static <T> String toJson(final T object) {
        try {
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            return "";
        }
    }
}