package db.migration;

import static org.jooq.impl.DSL.currentTimestamp;
import static org.jooq.impl.DSL.foreignKey;
import static org.jooq.impl.DSL.primaryKey;
import static org.jooq.impl.DSL.unique;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.TIMESTAMP;
import static org.jooq.impl.SQLDataType.VARCHAR;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V2021052905151622319358__create_table_user extends BaseJavaMigration {
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
