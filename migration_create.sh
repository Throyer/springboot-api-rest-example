#!/bin/sh
TIMESTAMP="./src/main/java/db/migration/`date +V%Y%m%d%I%M%s__$1`.java"
touch $TIMESTAMP
echo "package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class `date +V%Y%m%d%I%M%s__$1` extends BaseJavaMigration {
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        // my migration statements
    }
}" >> $TIMESTAMP