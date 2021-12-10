package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
* @see https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/
*/
public class V1639097360419__CreateTableUser extends BaseJavaMigration {
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        create.transaction(configuration -> {
            using(configuration)
                    .createTableIfNotExists("user")
                    .column("id", BIGINT.identity(true))
                    .column("name", VARCHAR(100).nullable(false))
                    .column("email", VARCHAR(100).nullable(true))
                    .column("deleted_email", VARCHAR(100).nullable(true))
                    .column("password", VARCHAR(100).nullable(false))
                    .column("active", BOOLEAN.defaultValue(true))
                    .column("created_at", TIMESTAMP.defaultValue(currentTimestamp()))
                    .column("updated_at", TIMESTAMP.nullable(true))
                    .column("deleted_at", TIMESTAMP.nullable(true))
                    .column("created_by", BIGINT.nullable(true))
                    .column("updated_by", BIGINT.nullable(true))
                    .column("deleted_by", BIGINT.nullable(true))
                    .constraints(
                        primaryKey("id"),
                        unique("email"),
                        foreignKey("created_by").references("user", "id"),
                        foreignKey("updated_by").references("user", "id"),
                        foreignKey("deleted_by").references("user", "id"))
                    .execute();
        });
    }
}