package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V2021012410361611538611__teste_java_based_migration extends BaseJavaMigration {
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        create.transaction(configuration -> {
            using(configuration)
                .createTable("my_new_table")
                    .column("id", BIGINT.identity(true))
                    .column("name", VARCHAR(255).nullable(false))
                .constraints(
                    primaryKey("id"),
                    unique("name"))
                .execute();
        });
    }
}
