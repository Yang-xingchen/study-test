package generator;

public class StopException extends RuntimeException {

    public static final StopException INSTANCE = new StopException();

    private StopException() {

    }

}
