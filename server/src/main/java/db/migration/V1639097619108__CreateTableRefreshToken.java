package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
 * @see "https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/"
 */
public class V1639097619108__CreateTableRefreshToken extends BaseJavaMigration {
  public void migrate(Context context) throws Exception {
    var create = using(context.getConnection());
    create.transaction(configuration -> {
      using(configuration)
        .createTableIfNotExists("refresh_token")
        .column("id", BIGINT.identity(true))
        .column("code", VARCHAR(40).nullable(false))
        .column("available", BOOLEAN.defaultValue(true))
        .column("expires_at", TIMESTAMP.nullable(false))
        .column("user_id", BIGINT.nullable(false))
        .constraints(
          constraint("refresh_token_pk").primaryKey("id"),
          constraint("refresh_token_unique_code").unique("code"),
          constraint("refresh_token_user_fk").foreignKey("user_id").references("user", "id"))
        .execute();
    });
  }
}