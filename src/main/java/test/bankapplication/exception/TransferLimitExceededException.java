package test.bankapplication.exception;

public class TransferLimitExceededException extends RuntimeException {
    public TransferLimitExceededException(String message) {
        super(message);
    }
}
