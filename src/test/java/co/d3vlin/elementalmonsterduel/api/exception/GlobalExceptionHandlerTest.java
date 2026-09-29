package co.d3vlin.elementalmonsterduel.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLTransientConnectionException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsDataAccessResourceFailureToServiceUnavailable() {
        ResponseEntity<?> response = handler.handleDatabaseUnavailable(
                new DataAccessResourceFailureException("no connection"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isEqualTo(java.util.Map.of("message", "Database is currently unreachable."));
    }

    @Test
    void mapsCannotCreateTransactionToServiceUnavailable() {
        ResponseEntity<?> response = handler.handleDatabaseUnavailable(
                new CannotCreateTransactionException("no connection"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void mapsSqlTransientConnectionExceptionToServiceUnavailable() {
        ResponseEntity<?> response = handler.handleDatabaseUnavailable(
                new SQLTransientConnectionException("timed out"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
