package com.github.throyer.common.springboot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JSON {

    private JSON() { }

    private static final ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
    
    public static <T> String stringify(final T object) {
        try {
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            return "";
        }
    }
}