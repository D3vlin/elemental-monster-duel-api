package co.d3vlin.elementalmonsterduel.api.card;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Patrón singleton de Testcontainers: un único Postgres real, arrancado una
 * sola vez por JVM de test y compartido por todas las subclases, en vez de
 * que cada una levante el suyo. Nunca se llama a {@code .stop()} a propósito
 * — Ryuk lo da de baja solo al terminar el proceso de test.
 */
abstract class AbstractCardDbIntegrationTest {

    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        POSTGRES.start();
    }

    protected static Connection connect() throws SQLException {
        return DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
    }
}
