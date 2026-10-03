package co.d3vlin.elementalmonsterduel.api.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLTransientConnectionException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Mock
    private HttpServletRequest request;

    @Test
    void mapsDataAccessResourceFailureToServiceUnavailable() {
        when(request.getRequestURI()).thenReturn("/cards");

        ResponseEntity<ApiError> response =
                handler.handleDatabaseUnavailable(new DataAccessResourceFailureException("no connection"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody().message()).isEqualTo("Database is currently unreachable.");
        assertThat(response.getBody().path()).isEqualTo("/cards");
    }

    @Test
    void mapsCannotCreateTransactionToServiceUnavailable() {
        when(request.getRequestURI()).thenReturn("/cards");

        ResponseEntity<ApiError> response =
                handler.handleDatabaseUnavailable(new CannotCreateTransactionException("no connection"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void mapsSqlTransientConnectionExceptionToServiceUnavailable() {
        when(request.getRequestURI()).thenReturn("/cards");

        ResponseEntity<ApiError> response =
                handler.handleDatabaseUnavailable(new SQLTransientConnectionException("timed out"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void mapsResourceNotFoundToNotFound() {
        when(request.getRequestURI()).thenReturn("/cards/999");

        ResponseEntity<ApiError> response =
                handler.handleResourceNotFound(new ResourceNotFoundException("Card not found: 999"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().message()).isEqualTo("Card not found: 999");
    }

    @Test
    void mapsUnsupportedLanguageToBadRequest() {
        when(request.getRequestURI()).thenReturn("/cards");

        ResponseEntity<ApiError> response =
                handler.handleUnsupportedLanguage(new UnsupportedLanguageException("fr"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().message()).isEqualTo("Unsupported lang: fr");
    }

    @Test
    void mapsMethodNotSupportedToMethodNotAllowed() {
        when(request.getRequestURI()).thenReturn("/cards");

        ResponseEntity<ApiError> response = handler.handleMethodNotSupported(
                new HttpRequestMethodNotSupportedException("POST"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Test
    void mapsValidationErrorToBadRequestWithFirstFieldMessage() {
        when(request.getRequestURI()).thenReturn("/cards");
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "lang", "must not be blank"));
        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException((org.springframework.core.MethodParameter) null, bindingResult);

        ResponseEntity<ApiError> response = handler.handleValidation(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().message()).isEqualTo("must not be blank");
    }

    @Test
    void mapsUnexpectedExceptionToGenericInternalServerError() {
        when(request.getRequestURI()).thenReturn("/cards");

        ResponseEntity<ApiError> response =
                handler.handleUnexpected(new IllegalStateException("some internal detail"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().message()).isEqualTo("An unexpected error occurred.");
        assertThat(response.getBody().message()).doesNotContain("some internal detail");
    }
}
