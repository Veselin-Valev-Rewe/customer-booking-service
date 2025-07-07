package com.example.customerbookingservice.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static ProblemDetail createProblemDetail(HttpStatus status, String title, String detail, Map<String, Object> properties) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        properties.forEach(problemDetail::setProperty);

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existing, replacement) -> replacement
                ));

        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                "One or more validation errors occurred.",
                Map.of(
                        "path", request.getRequestURI(),
                        "errors", errors
                ));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleAllExceptions(HttpServletRequest request, Exception ex) {
        return createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                ex.getMessage(),
                Map.of(
                        "exception", ex.getClass().getSimpleName(),
                        "path", request.getRequestURI()
                ));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleEntityNotFound(HttpServletRequest request, EntityNotFoundException ex) {
        return createProblemDetail(
                HttpStatus.NOT_FOUND,
                "Entity not found",
                ex.getMessage(),
                Map.of(
                        "exception", ex.getClass().getSimpleName(),
                        "path", request.getRequestURI()
                ));
    }
}
