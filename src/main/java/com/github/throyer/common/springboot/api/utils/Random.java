package com.github.throyer.common.springboot.api.utils;

import static com.github.throyer.common.springboot.api.domain.builders.UserBuilder.createUser;

import java.util.List;
import java.util.Locale;

import com.github.javafaker.Faker;
import com.github.throyer.common.springboot.api.domain.models.entity.Role;
import com.github.throyer.common.springboot.api.domain.models.entity.User;

public class Random {
        
    public static final Integer MAXIMUM_PASSWORD_LENGTH = 8;
    public static final Integer MINIMUM_PASSWORD_LENGTH = 18;
    public static final Boolean HAS_UPPERCASE = true;
    public static final Boolean HAS_SPECIAL_CHARACTERS = true;
    public static final Boolean HAS_DIGITS = true;

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

    public static User randomUser() {
        return randomUser(List.of());
    }

    public static User randomUser(List<Role> roles) {
        var builder = createUser(FAKER.name().fullName())
            .setEmail(FAKER.internet().safeEmailAddress())
            .setPassword(FAKER.internet().password(
                    MAXIMUM_PASSWORD_LENGTH,
                    MINIMUM_PASSWORD_LENGTH,
                    HAS_UPPERCASE,
                    HAS_SPECIAL_CHARACTERS,
                    HAS_DIGITS
            ));
        
        roles.forEach(builder::addRole);
        
        return builder.build();
    }
}
