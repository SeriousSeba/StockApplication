package pl.agh.edu.stockinger.exception;

public class MissingCompanyException extends Exception{
    public MissingCompanyException(String message) {
        super(message);
    }

    public MissingCompanyException(String message, Throwable cause) {
        super(message, cause);
    }
}
