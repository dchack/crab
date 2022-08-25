package executor;


import storage.IdempotentRecordStorage;
import storage.StorageFactory;
import storage.StorageTypeEnum;

import java.util.function.Supplier;

/**
 * Default executor idempotent
 *
 * @author hackdc
 * @Date 2022/8/24 8:21 PM
 */
public class DefaultIdempotentExecutor implements IdempotentExecutor{

    private static final String LOCK_VALUE = "1";

    private static final long DEFAULT_LOCK_TIME = 60000;

    private IdempotentRecordStorage idempotentRecordStorage;

//    private RedisLockService redisLockService;

    private StorageFactory storageFactory;

//    public DefaultIdempotentExecutor(RedisLockService redisLockService, StorageFactory storageFactory) {
//        this.redisLockService = redisLockService;
//        this.storageFactory = storageFactory;
//    }


    public DefaultIdempotentExecutor(IdempotentRecordStorage idempotentRecordStorage) {
        this.idempotentRecordStorage = idempotentRecordStorage;
    }

    @Override
    public <T> T execute(IdempotentRequest request, Supplier<T> processSupplier, Supplier<T> failSupplier) {
        String idempotentKey = request.getKey();
        long idempotentExpire = request.getIdempotentExpire();
        long lockExpire = request.getLockExpire() == 0 ? DEFAULT_LOCK_TIME : request.getLockExpire();
        IdempotentRecordStorage idempotentRecordStorage = getIdempotentRecordStorage(idempotentExpire);
        // todo lock
//        try {
//            boolean locked = redisLockService.lock(idempotentKey, LOCK_VALUE, lockExpire, true);
//            if (locked) {
                if (idempotentRecordStorage.hasKey(idempotentKey)) {
                    return failSupplier.get();
                }
                T result = processSupplier.get();
                idempotentRecordStorage.setKey(idempotentKey, idempotentExpire);
                return result;
//            } else {
//                return failSupplier.get();
//            }
//        } finally {
//            redisLockService.unlock(idempotentKey, LOCK_VALUE, true);
//        }

    }

    /**
     * If it has expired time, recommend to use redis
     * @param idempotentExpire
     * @return
     */
    private IdempotentRecordStorage getIdempotentRecordStorage(long idempotentExpire) {
        IdempotentRecordStorage idempotentRecordStorage;
        if (idempotentExpire == 0) {
            idempotentRecordStorage = storageFactory.get(StorageTypeEnum.ORACLE);
        } else {
            idempotentRecordStorage = storageFactory.get(StorageTypeEnum.REDIS);
        }
        return idempotentRecordStorage;
    }
}
