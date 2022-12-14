package com.crab.cache.multi;

import lombok.Data;

import java.io.Serializable;

/**
 * Use to store cache value
 * (isNull == 1) is represented value is null and stored in cache
 *
 * @author hackdc
 * @Date 2022/9/1 2:22 PM
 */
@Data
public class Item<T> implements Serializable {

    private T value;
    private byte isNull;

    /**
     * support item created for store null value
     * @param value
     */
    public Item(T value) {
        this.value = value;
        this.isNull = value == null ? (byte)1 : 0;
    }

    public Item() {
    }

    public T get() {
        return value;
    }

    public boolean isNull(){
        return this.value == null;
    }
}
