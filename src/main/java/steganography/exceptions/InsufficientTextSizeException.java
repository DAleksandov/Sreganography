package steganography.exceptions;

public class InsufficientTextSizeException extends Exception {
    public InsufficientTextSizeException() {
    }

    public InsufficientTextSizeException(String message) {
        super(message);
    }
}
