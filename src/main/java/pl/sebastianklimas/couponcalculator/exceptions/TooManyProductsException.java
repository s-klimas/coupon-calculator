package pl.sebastianklimas.couponcalculator.exceptions;

public class TooManyProductsException extends RuntimeException {
    public TooManyProductsException(String message) {
        super(message);
    }
}
