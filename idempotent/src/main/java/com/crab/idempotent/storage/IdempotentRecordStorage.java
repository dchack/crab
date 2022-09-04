package com.crab.idempotent.storage;

/**
 * Idempotent record
 *
 * @author hackdc
 * @Date 2022/8/24 7:04 PM
 */
public interface IdempotentRecordStorage {

    /**
     * Expire idempotent key storage
     * @param key
     * @param expire 秒
     */
    void setKey(String key, long expire);

    /**
     * Verify that the execution is complete
     * @param key
     * @return
     */
    boolean hasKey(String key);

    /**
     * Get type
     * @return
     */
    StorageTypeEnum getType();
}
