package co.d3vlin.elementalmonsterduel.api.card;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CardNameTranslationIntegrityTest extends AbstractCardDbIntegrationTest {

    private static final Path DB_DDL_DIR = Path.of("..", "db", "ddl", "tables");
    private static final Path DB_DML_DIR = Path.of("..", "db", "dml", "tables");
    private static final List<String> TABLES =
            List.of("locale", "element", "power_rank", "element_translation", "power_rank_translation");

    @BeforeAll
    static void applySchemaAndRealSeedData() throws Exception {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            for (String table : TABLES) {
                statement.execute(Files.readString(DB_DDL_DIR.resolve(table + ".sql")));
            }
            for (String table : TABLES) {
                statement.execute(Files.readString(DB_DML_DIR.resolve(table + ".sql")));
            }
        }
    }

    private List<String> query(String sql) throws SQLException {
        List<String> rows = new ArrayList<>();
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                rows.add(resultSet.getString(1) + "/" + resultSet.getString(2));
            }
        }
        return rows;
    }

    @Test
    void everyElementHasATranslationForEveryLocale() throws SQLException {
        assertThat(query(
                        "SELECT e.code, l.code FROM element e CROSS JOIN locale l "
                                + "LEFT JOIN element_translation et ON et.element = e.code AND et.locale_id = l.id "
                                + "WHERE et.element IS NULL"))
                .as("element/locale combinations missing from element_translation")
                .isEmpty();
    }

    @Test
    void everyPowerRankHasATranslationForEveryLocale() throws SQLException {
        assertThat(query(
                        "SELECT pr.code, l.code FROM power_rank pr CROSS JOIN locale l "
                                + "LEFT JOIN power_rank_translation prt ON prt.power_rank = pr.code AND prt.locale_id = l.id "
                                + "WHERE prt.power_rank IS NULL"))
                .as("power_rank/locale combinations missing from power_rank_translation")
                .isEmpty();
    }

    @Test
    void noElementTranslationLabelIsBlank() throws SQLException {
        assertThat(query("SELECT element, locale_id::text FROM element_translation WHERE TRIM(label) = ''"))
                .as("element_translation rows with a blank label")
                .isEmpty();
    }

    @Test
    void noPowerRankTranslationLabelIsBlank() throws SQLException {
        assertThat(query("SELECT power_rank, locale_id::text FROM power_rank_translation WHERE TRIM(label) = ''"))
                .as("power_rank_translation rows with a blank label")
                .isEmpty();
    }

    @Test
    void noTwoElementsShareTheSameLabelInTheSameLocale() throws SQLException {
        assertThat(query(
                        "SELECT label, locale_id::text FROM element_translation "
                                + "GROUP BY locale_id, label HAVING COUNT(*) > 1"))
                .as("labels duplicated across different elements within the same locale")
                .isEmpty();
    }

    @Test
    void noTwoPowerRanksShareTheSameLabelInTheSameLocale() throws SQLException {
        assertThat(query(
                        "SELECT label, locale_id::text FROM power_rank_translation "
                                + "GROUP BY locale_id, label HAVING COUNT(*) > 1"))
                .as("labels duplicated across different power ranks within the same locale")
                .isEmpty();
    }
}
