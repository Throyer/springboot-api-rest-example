package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V2021052906011622322101__create_table_userRole extends BaseJavaMigration {
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        create.transaction(configuration -> {
            using(configuration)
                .createTableIfNotExists("user_role")
                    .column("user_id", BIGINT.nullable(true))
                    .column("role_id", BIGINT.nullable(true))
                .constraints(
                    foreignKey("user_id").references("user", "id"),
                    foreignKey("role_id").references("role", "id"))
                .execute();
        });
    }
}
