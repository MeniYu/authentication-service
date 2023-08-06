package io.incondensable.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abbas
 */
@RestControllerAdvice
public class BusinessExceptionHandler {

    private final MessageSource messageSource;

    public BusinessExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(ex.getHttpStatusCode())
                .body(messageSource.getMessage(ex.getMessage(), ex.getArgs(), ex.getLocale()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> validationException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<String> errors = new ArrayList<>(fieldErrors.size());
        for (FieldError fieldError : fieldErrors) {
            errors.add(fieldError.getField());
        }
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errors);
    }

}
