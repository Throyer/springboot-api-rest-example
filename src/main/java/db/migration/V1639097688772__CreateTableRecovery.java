package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
* @see https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/
*/
public class V1639097688772__CreateTableRecovery extends BaseJavaMigration {
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        create.transaction(configuration -> {
            using(configuration)
                .createTableIfNotExists("recovery")
                .column("id", BIGINT.identity(true))
                .column("code", VARCHAR(4).nullable(false))
                .column("expires_in", TIMESTAMP.nullable(false))
                .column("user_id", BIGINT.nullable(false))
                .constraints(
                    primaryKey("id"),
                    unique("code"),
                    foreignKey("user_id").references("user", "id"))
                .execute();
        });
    }
}