package ru.codeportfolio.exceptions;

public class NextStudyException extends RuntimeException {
    public NextStudyException(String message) {
        super(message);
    }

    public NextStudyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NextStudyException(Throwable cause) {
        super(cause);
    }

    public NextStudyException() {
    }
}
