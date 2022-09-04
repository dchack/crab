package com.crab.idempotent.executor;

/**
 *
 * @author hackdc
 * @Date 2022/8/24 8:22 PM
 */
public class IdempotentRequest {

    private String key;

    private long idempotentExpire;

    private long lockExpire;

    public IdempotentRequest setKey(String key) {
        this.key = key;
        return this;
    }

    public IdempotentRequest setIdempotentExpire(long idempotentExpire) {
        this.idempotentExpire = idempotentExpire;
        return this;
    }

    public IdempotentRequest setLockExpire(long lockExpire) {
        this.lockExpire = lockExpire;
        return this;
    }

    public long getIdempotentExpire() {
        return idempotentExpire;
    }

    public String getKey() {
        return key;
    }

    public long getLockExpire() {
        return lockExpire;
    }
}
