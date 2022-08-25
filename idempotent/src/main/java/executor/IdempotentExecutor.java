package executor;

import java.util.function.Supplier;

/**
 * Idempotent executor
 *
 * @author hackdc
 * @Date 2022/8/24 8:21 PM
 */
public interface IdempotentExecutor {


    <T> T execute (IdempotentRequest request, Supplier<T> processSupplier, Supplier<T> failSupplier);

}
