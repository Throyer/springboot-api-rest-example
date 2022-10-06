package com.github.throyer.example.modules.authentication.repositories;

public class Queries {
  private Queries() { }
    
  public static final String DISABLE_OLD_REFRESH_TOKENS_FROM_USER = """
    UPDATE
      RefreshToken
    SET
      available = false
    WHERE
      user_id = ?1 AND available = true
  """;
    
  public static final String FIND_REFRESH_TOKEN_BY_CODE_FETCH_USER_AND_ROLES = """
    SELECT refresh FROM RefreshToken refresh
    JOIN FETCH refresh.user user
    JOIN FETCH user.roles
    WHERE refresh.code = ?1 AND refresh.available = true
  """;
}
