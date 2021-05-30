package db.migration;

import static org.jooq.impl.DSL.currentTimestamp;
import static org.jooq.impl.DSL.foreignKey;
import static org.jooq.impl.DSL.primaryKey;
import static org.jooq.impl.DSL.unique;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.BIT;
import static org.jooq.impl.SQLDataType.TIMESTAMP;
import static org.jooq.impl.SQLDataType.VARCHAR;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V2021052905531622321601__create_table_role extends BaseJavaMigration {
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        create.transaction(configuration -> {
            using(configuration)
                .createTableIfNotExists("role")
                    .column("id", BIGINT.identity(true))
                    .column("name", VARCHAR(100).nullable(false))
                    .column("deleted_name", VARCHAR(100).nullable(true))
                    .column("initials", VARCHAR(100).nullable(false))
                    .column("deleted_initials", VARCHAR(100).nullable(true))
                    .column("description", VARCHAR(100).nullable(true))
                    .column("active", BIT.defaultValue(true))
                    .column("created_at", TIMESTAMP.defaultValue(currentTimestamp()))
                    .column("updated_at", TIMESTAMP.nullable(true))
                    .column("deleted_at", TIMESTAMP.nullable(true))
                    .column("created_by", BIGINT.nullable(true))
                    .column("updated_by", BIGINT.nullable(true))
                    .column("deleted_by", BIGINT.nullable(true))
                .constraints(
                    primaryKey("id"),
                    unique("name"),
                    unique("initials"),
                    foreignKey("created_by").references("user", "id"),
                    foreignKey("updated_by").references("user", "id"),
                    foreignKey("deleted_by").references("user", "id"))
                .execute();
        });
    }
}
