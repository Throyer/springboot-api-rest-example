package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
* @see https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/
*/
public class V1639097454131__CreateTableRole extends BaseJavaMigration {
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
                        .column("active", BOOLEAN.defaultValue(true))
                        .column("created_at", TIMESTAMP.defaultValue(currentTimestamp()))
                        .column("updated_at", TIMESTAMP.nullable(true))
                        .column("deleted_at", TIMESTAMP.nullable(true))
                        .column("created_by", BIGINT.nullable(true))
                        .column("updated_by", BIGINT.nullable(true))
                        .column("deleted_by", BIGINT.nullable(true))
                    .constraints(
                        constraint("role_pk").primaryKey("id"),                            
                        constraint("role_unique_name").unique("name"),                        
                        constraint("role_unique_initials").unique("initials"),                        
                        constraint("role_created_by_fk").foreignKey("created_by").references("user", "id"),                        
                        constraint("role_updated_by_fk").foreignKey("updated_by").references("user", "id"),                        
                        constraint("role_deleted_by_fk").foreignKey("deleted_by").references("user", "id"))
                    .execute();
        });
    }
}