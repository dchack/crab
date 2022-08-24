package executor;


import storage.IdempotentRecordStorage;

import java.util.function.Supplier;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/8/24 8:21 PM
 */
public class DefaultIdempotentExecutor implements IdempotentExecutor{

    private IdempotentRecordStorage idempotentRecordStorage;

    public DefaultIdempotentExecutor(IdempotentRecordStorage idempotentRecordStorage) {
        this.idempotentRecordStorage = idempotentRecordStorage;
    }

    @Override
    public <T> T execute(IdempotentRequest request, Supplier<T> processSupplier, Supplier<T> failSupplier) {
        // todo 分布式锁保护
        String idempotentKey = request.getKey();
        long expire = request.getExpire();
        if (idempotentRecordStorage.hasKey(idempotentKey)) {
            return failSupplier.get();
        }
        T result = processSupplier.get();
        idempotentRecordStorage.setKey(idempotentKey, expire);
        return result;
    }
}
