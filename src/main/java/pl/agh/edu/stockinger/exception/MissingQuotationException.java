package pl.agh.edu.stockinger.exception;

public class MissingQuotationException extends Exception{
    public MissingQuotationException() {
        super();
    }

    public MissingQuotationException(String message) {
        super(message);
    }
}
