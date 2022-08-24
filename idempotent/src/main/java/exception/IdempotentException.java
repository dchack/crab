package exception;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/8/24 8:41 PM
 */
public class IdempotentException extends RuntimeException {

    public IdempotentException(String message) {
        super(message);
    }

}
