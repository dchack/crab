package com.crab.cache.multi;

import lombok.Data;

import java.io.Serializable;

/**
 * use Item to store cache value
 *
 * @author dongchao
 * @Date 2022/9/1 2:22 PM
 */
@Data
public class Item<T> implements Serializable {

    private T value;

    public Item(T value) {
        this.value = value;
    }

    public Item() {
    }

    public T get() {
        return value;
    }
}
