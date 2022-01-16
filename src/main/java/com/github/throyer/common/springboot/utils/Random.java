package com.github.throyer.common.springboot.utils;

import java.util.List;
import java.util.Locale;

import com.github.javafaker.Faker;
import com.github.throyer.common.springboot.domain.role.entity.Role;
import com.github.throyer.common.springboot.domain.user.entity.User;

public class Random {
        
    private static final java.util.Random RANDOM = new java.util.Random();
    public static final Faker FAKER = new Faker(new Locale("pt", "BR"));

    public static Integer between(Integer min, Integer max) {
        return RANDOM.nextInt(max - min) + min;
    }

    public static <T> T getRandomElement(List<T> itens) {
        return itens.get(RANDOM.nextInt(itens.size()));
    }

    public static String code() {
        return String.format("%s%s%s%s", between(0, 9), between(0, 9), between(0, 9), between(0, 9));
    }

    public static String password() {
        return FAKER.regexify("[a-z]{5,13}[1-9]{1,5}[A-Z]{1,5}[#?!@$%^&*-]{1,5}");
    }

    public static User randomUser() {
        return randomUser(List.of());
    }

    public static User randomUser(List<Role> roles) {
        return new User(
            FAKER.name().fullName(),
            FAKER.internet().safeEmailAddress(),
            password(),
            roles
        );
    }
}
