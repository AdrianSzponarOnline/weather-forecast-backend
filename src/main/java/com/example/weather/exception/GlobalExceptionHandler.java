package com.example.weather.exception;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgValidation(
            MethodArgumentNotValidException ex) {

        List<Map<String, String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(err -> Map.of(
                        "field",   err.getField(),
                        "message", Objects.requireNonNull(err.getDefaultMessage())))
                .toList();

        return build(HttpStatus.BAD_REQUEST,
                "Niepoprawne parametry wejściowe",
                fieldErrors);
    }

    @ExceptionHandler({ HttpClientErrorException.class,
                        HttpServerErrorException.class,
                        ResourceAccessException.class })
    public ResponseEntity<Map<String, Object>> handleUpstream(RestClientException ex) {
        return build(HttpStatus.BAD_GATEWAY,
                "Serwis pogodowy niedostępny",
                "Spróbuj ponownie za kilka minut");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("Constraint violation: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "Niepoprawne parametry wejściowe", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex) {
        log.error("Unexpected error", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Wewnętrzny błąd serwera", "Wystąpił nieoczekiwany błąd");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        return build(HttpStatus.BAD_REQUEST, "Brak wymaganych parametrów", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status,
                                                     String message,
                                                     Object details) {
        return ResponseEntity.status(status).body(
                Map.of("timestamp", Instant.now(),
                        "status",    status.value(),
                        "error",     status.getReasonPhrase(),
                        "message",   message,
                        "details",   details));
    }
}
