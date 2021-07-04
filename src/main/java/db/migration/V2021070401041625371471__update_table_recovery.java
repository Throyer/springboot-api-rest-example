package db.migration;

import static org.jooq.impl.DSL.using;
import static org.jooq.impl.SQLDataType.BOOLEAN;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V2021070401041625371471__update_table_recovery extends BaseJavaMigration {
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        create.transaction(configuration -> {
            using(configuration)
                .alterTable("recovery")
                .addColumn("confirmed", BOOLEAN.nullable(true))
                    .after("code")
                        .execute();
    
            using(configuration)
                .alterTable("recovery")
                .addColumn("used", BOOLEAN.nullable(true))
                    .after("confirmed")
                        .execute();
        });
    }
}
