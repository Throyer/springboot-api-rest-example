package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
* @see https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/
*/
public class V1639098081278__UpdateTableRecovery extends BaseJavaMigration {
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        create.transaction(configuration -> {
            using(configuration)
                .alterTable("recovery")
                    .addColumn("confirmed", BOOLEAN.nullable(true))
                        .execute();

            using(configuration)
                .alterTable("recovery")
                    .addColumn("used", BOOLEAN.nullable(true))
                        .execute();
        });
    }
}