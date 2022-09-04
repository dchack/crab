package com.crab.idempotent.storage;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author hackdc
 * @Date 2022/8/25 1:24 PM
 */
public class StorageFactory {

    private List<IdempotentRecordStorage> idempotentRecordStorageList;

    public StorageFactory(List<IdempotentRecordStorage> idempotentRecordStorageList) {
        this.idempotentRecordStorageList = idempotentRecordStorageList;
    }

    /**
     * Get storage by type
     * @param type
     * @return
     */
    public IdempotentRecordStorage get(StorageTypeEnum type) {
        Optional<IdempotentRecordStorage> optional = idempotentRecordStorageList.stream().filter(t -> t.getType() == type).findFirst();
        return optional.orElseThrow(NullPointerException::new);
    }

}
