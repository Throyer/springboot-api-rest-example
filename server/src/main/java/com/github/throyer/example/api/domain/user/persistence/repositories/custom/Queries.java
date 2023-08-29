package com.github.throyer.example.api.domain.user.persistence.repositories.custom;

public class Queries {
  public Queries() { }
  
  //language=sql
  public static final String SELECT_IDS_FROM_USERS = "select u.id from user u";

  //language=sql
  public static final String SELECT_DISTINCT_USERS_FETCH_ROLES = """
    select distinct u from user u
    join fetch u.roles
    where u.id in (:user_ids) order by u.id
  """;

  //language=sql
  public static final String COUNT_IDS_FROM_USERS = "select count(u.id) from user u";
}
