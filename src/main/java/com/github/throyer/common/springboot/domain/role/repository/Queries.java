package com.github.throyer.common.springboot.domain.role.repository;

public class Queries {
    public static final String DELETE_ROLE_BY_ID = """
        UPDATE 
            #{#entityName}
        SET
            deleted_name = (
                SELECT name FROM #{#entityName} WHERE id = ?1
            ),
            name = NULL,
            deleted_initials = (
                SELECT initials FROM #{#entityName} WHERE id = ?1
            ),
            initials = NULL,
            deleted_at = CURRENT_TIMESTAMP,
            active = false,
            deleted_by = ?#{principal?.id}
        WHERE id = ?1
    """;
}
