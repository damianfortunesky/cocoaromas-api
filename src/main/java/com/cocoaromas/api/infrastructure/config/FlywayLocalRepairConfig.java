package com.cocoaromas.api.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.exception.FlywayValidateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class FlywayLocalRepairConfig {

    @Bean
    public FlywayMigrationStrategy flywayLocalMigrationStrategy(
            @Value("${app.flyway.auto-repair-on-validation-error:true}") boolean autoRepairOnValidationError
    ) {
        return flyway -> migrateWithOptionalRepair(flyway, autoRepairOnValidationError);
    }

    private void migrateWithOptionalRepair(Flyway flyway, boolean autoRepairOnValidationError) {
        try {
            flyway.migrate();
        } catch (FlywayValidateException ex) {
            if (!autoRepairOnValidationError) {
                throw ex;
            }
            flyway.repair();
            flyway.migrate();
        }
    }
}
