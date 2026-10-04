package co.d3vlin.elementalmonsterduel.api.card;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class CardSchemaConstraintsTest {

    private static final Path DB_DDL_DIR = Path.of("..", "db", "ddl", "tables");

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void applySchema() throws Exception {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            for (String table : List.of("element", "power_rank", "card")) {
                statement.execute(Files.readString(DB_DDL_DIR.resolve(table + ".sql")));
            }
        }
    }

    @BeforeEach
    void seedReferenceData() throws SQLException {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO public.element (code) VALUES ('FIRE') ON CONFLICT DO NOTHING");
            statement.execute("INSERT INTO public.power_rank (code) VALUES ('SPAWN') ON CONFLICT DO NOTHING");
        }
    }

    @AfterEach
    void cleanCardRows() throws SQLException {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM public.card");
        }
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
    }

    private void insertCard(int atk, int armor, int life, int seal, String element, String powerRank)
            throws SQLException {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(String.format(
                    "INSERT INTO public.card (element, power_rank, atk, armor, life, seal) "
                            + "VALUES ('%s', '%s', %d, %d, %d, %d)",
                    element, powerRank, atk, armor, life, seal));
        }
    }

    @Test
    void acceptsAValidCard() throws SQLException {
        insertCard(10, 5, 20, 0, "FIRE", "SPAWN");
    }

    @Test
    void acceptsZeroSealAsTheAllowedBoundary() throws SQLException {
        insertCard(10, 5, 20, 0, "FIRE", "SPAWN");
    }

    @Test
    void rejectsNonPositiveAtk() {
        assertThatThrownBy(() -> insertCard(0, 5, 20, 0, "FIRE", "SPAWN"))
                .hasMessageContaining("ck_card_atk_positive");
    }

    @Test
    void rejectsNonPositiveArmor() {
        assertThatThrownBy(() -> insertCard(10, 0, 20, 0, "FIRE", "SPAWN"))
                .hasMessageContaining("ck_card_armor_positive");
    }

    @Test
    void rejectsNonPositiveLife() {
        assertThatThrownBy(() -> insertCard(10, 5, 0, 0, "FIRE", "SPAWN"))
                .hasMessageContaining("ck_card_life_positive");
    }

    @Test
    void rejectsNegativeSeal() {
        assertThatThrownBy(() -> insertCard(10, 5, 20, -1, "FIRE", "SPAWN"))
                .hasMessageContaining("ck_card_seal_non_negative");
    }

    @Test
    void rejectsAnElementThatDoesNotExist() {
        assertThatThrownBy(() -> insertCard(10, 5, 20, 0, "NOT_AN_ELEMENT", "SPAWN"))
                .hasMessageContaining("fk_card_element");
    }

    @Test
    void rejectsAPowerRankThatDoesNotExist() {
        assertThatThrownBy(() -> insertCard(10, 5, 20, 0, "FIRE", "NOT_A_POWER_RANK"))
                .hasMessageContaining("fk_card_power_rank");
    }

    @Test
    void rejectsADuplicateElementPowerRankCombination() throws SQLException {
        insertCard(10, 5, 20, 0, "FIRE", "SPAWN");

        assertThatThrownBy(() -> insertCard(99, 99, 99, 0, "FIRE", "SPAWN"))
                .hasMessageContaining("uq_card_element_power_rank");
    }
}
