package pl.agh.edu.stockinger.exception;

public class MissingQuotation extends Exception{
    public MissingQuotation() {
        super();
    }

    public MissingQuotation(String message) {
        super(message);
    }
}
