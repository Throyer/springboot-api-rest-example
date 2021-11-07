package com.github.throyer.common.springboot.domain.models.pagination;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SortableProperty {
    public String name() default "";

    public String column() default "";
}
