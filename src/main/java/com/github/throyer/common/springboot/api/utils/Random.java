package com.github.throyer.common.springboot.api.utils;

public class Random {
    public static Integer between(Integer min, Integer max) {
        return new java.util.Random().nextInt(max - min) + min;
    }

    public static String code() {
        return String.format("%s%s%s%s", between(0, 9), between(0, 9), between(0, 9), between(0, 9));
    }
}
