package pl.sebastianklimas.couponcalculator.exceptions;

public class TooManyCouponsException extends RuntimeException {
    public TooManyCouponsException(String message) {
        super(message);
    }
}