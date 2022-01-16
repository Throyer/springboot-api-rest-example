package com.github.throyer.common.springboot.domain.management.repository;

public class Queries {
    public static final String DELETE_BY_ID = """
        UPDATE
            #{#entityName}
        SET
            deleted_at = CURRENT_TIMESTAMP
        WHERE id = ?1
    """;
    
    public static final String DELETE_ALL = """
        UPDATE
            #{#entityName}
        SET
            deleted_at = CURRENT_TIMESTAMP
    """;
    
    public static final String NON_DELETED_CLAUSE = "deleted_at IS NULL";
}
