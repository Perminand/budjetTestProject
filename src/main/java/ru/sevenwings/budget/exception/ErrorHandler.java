package ru.sevenwings.budget.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestError(final ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorMessage", e.getLocalizedMessage());
        errors.put("stackTrace", getStackTraceAsString(e));
        ErrorResponse errorResponse = new ErrorResponse(errors, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        log.error(e.getLocalizedMessage());
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestError(final MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorMessage", e.getLocalizedMessage());
        errors.put("stackTrace", getStackTraceAsString(e));
        ErrorResponse errorResponse = new ErrorResponse(errors, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        log.error(e.getLocalizedMessage());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllException(Exception e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorMessage", e.getLocalizedMessage());
        errors.put("stackTrace", getStackTraceAsString(e));
        ErrorResponse errorResponse = new ErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
        log.error(e.getLocalizedMessage());
        return errorResponse;
    }


    private String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
