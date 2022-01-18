package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
* @see https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/
*/
public class V1639097544500__CreateTableUserRoleManyToMany extends BaseJavaMigration {
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        create.transaction(configuration -> {
            using(configuration)
                .createTableIfNotExists("user_role")
                    .column("user_id", BIGINT.nullable(true))
                    .column("role_id", BIGINT.nullable(true))
                .constraints(
                    constraint("user_role_fk").foreignKey("user_id").references("user", "id"),
                    constraint("role_user_fk").foreignKey("role_id").references("role", "id"))
                .execute();
        });
    }
}