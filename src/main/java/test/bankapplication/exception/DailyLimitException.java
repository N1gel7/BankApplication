package test.bankapplication.exception;

public class DailyLimitException extends RuntimeException {
    public DailyLimitException(String message) {
        super(message);
    }
}
