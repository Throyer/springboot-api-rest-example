package com.github.throyer.common.springboot.utils;

import static org.jooq.impl.DSL.field;

import java.util.Optional;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;

public class Migrations {
    public static Optional<Long> findOptionalId(DSLContext dsl, Table<Record> table, Condition condition) {
        return dsl.select(field("id"))
            .from(table)
                .where(condition)
            .limit(1)
                .fetchOptional(field("id"), Long.class);
            
    }
}
