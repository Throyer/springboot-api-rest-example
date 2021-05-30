package db.migration;

import static com.github.throyer.common.springboot.api.utils.Migrations.findOptionalId;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.using;

import com.github.throyer.common.springboot.api.models.entity.User;
import com.github.throyer.common.springboot.api.services.user.dto.CreateUser;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class V2021052906051622322329__insert_admin_and_roles extends BaseJavaMigration {
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        create.transaction(configuration -> {

            var admin = new CreateUser("admin", "admin@email.com", "admin")
                .toUser();

            var encoder = new BCryptPasswordEncoder(User.PASSWORD_STRENGTH);

            var dsl = using(configuration);

            dsl.insertInto(
                table("user"),
                    field("name"),
                    field("email"),
                    field("password")
                )
                .values(admin.getName(), admin.getEmail(), encoder.encode(admin.getPassword()))
            .execute();

            dsl.insertInto(
                table("role"),
                    field("name"),
                    field("initials"),
                    field("description"),
                    field("created_by")
                )
                .values("ADMINISTRADOR", "ADM", "Administrador",
                    findOptionalId(dsl, 
                        table("user"), 
                            field("email").equal(admin.getEmail()))
                    .orElseThrow())
                .values("USER", "USER", "Usuário",
                    findOptionalId(dsl,
                        table("user"),
                            field("email").equal(admin.getEmail()))
                    .orElseThrow())
            .execute();

            dsl.insertInto(
                table("user_role"),
                    field("user_id"),
                    field("role_id")
                )
                .values(
                    findOptionalId(dsl, table("user"), field("email").equal("admin@email.com"))
                        .orElseThrow(),
                    findOptionalId(dsl, table("role"), field("initials").equal("ADM"))
                        .orElseThrow()
                )
                .values(
                    findOptionalId(dsl, table("user"), field("email").equal("admin@email.com"))
                        .orElseThrow(),
                    findOptionalId(dsl, table("role"), field("initials").equal("USER"))
                        .orElseThrow()
                )
            .execute();
        });
    }
}
