package com.github.throyer.common.springboot.api.utils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;

public class SQLUtils {

    private static String REPLACES = "áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ";
    private static String MATCHES = "aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC";

    public static Expression<String> replace(
        CriteriaBuilder builder,
        Expression<String> path
    ) {
        return builder
            .function(
                "replace",
                String.class,
                builder.lower(path),
                builder.literal(REPLACES),
                builder.literal(MATCHES)
            );
    }
}
