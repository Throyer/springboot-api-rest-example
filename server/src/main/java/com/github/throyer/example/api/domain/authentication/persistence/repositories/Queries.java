package com.github.throyer.example.api.domain.authentication.persistence.repositories;

public class Queries {
  private Queries() { }
  
  //language=sql
  public static final String DISABLE_OLD_REFRESH_TOKENS_FROM_USER = """
    update
      refresh_token refresh
    set
      refresh.available = false
    where
      refresh.user.id = :user_id and refresh.available = true
  """;

  //language=sql
  public static final String FIND_REFRESH_TOKEN_BY_CODE_FETCH_USER_AND_ROLES = """
    select
      refresh
    from
      refresh_token refresh
    join fetch refresh.user user
    join fetch user.roles
    where
      refresh.code = :code and refresh.available = true
  """;
}
