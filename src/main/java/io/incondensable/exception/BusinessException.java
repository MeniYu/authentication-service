package io.incondensable.exception;

import java.util.Locale;

/**
 * @author abbas
 */
public class BusinessException extends RuntimeException {

    private final String message;
    private final Object[] args;
    private final Locale locale;
    private final int httpStatusCode;

    public BusinessException(String message, Object[] args, Locale locale, int httpStatusCode) {
        this.message = message;
        this.args = args;
        this.locale = locale;
        this.httpStatusCode = httpStatusCode;
    }

    public BusinessException(BusinessException e) {
        this.message = e.getMessage();
        this.args = e.getArgs();
        this.locale = e.getLocale();
        this.httpStatusCode = e.getHttpStatusCode();
    }

    public String getMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }

    public Locale getLocale() {
        return locale;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
