package com.github.throyer.example.modules.roles.repositories;

public class Queries {
  public static final String DELETE_ROLE_BY_ID = """
    UPDATE 
      #{#entityName}
    SET
      deletedName = (
        SELECT name FROM #{#entityName} WHERE id = ?1
      ),
      name = NULL,
      deletedShortName = (
        SELECT shortName FROM #{#entityName} WHERE id = ?1
      ),
      shortName = NULL,
      deletedAt = CURRENT_TIMESTAMP,
      active = false,
      deletedBy = ?#{principal?.id}
    WHERE id = ?1
  """;
}
