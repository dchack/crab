package com.crab.cache.multi;

/**
 * Cache item wrapper, support record hit level
 *
 * @author hackdc
 * @Date 2022/7/30 11:39 PM
 */
public class CacheResult<T> {

    public final static byte LEVEL_1 	 = 1;
    public final static byte LEVEL_2 	 = 2;
    public final static byte LEVEL_OUTER = 3;

    private Item<T> value;

    private byte level;

    public CacheResult(byte level) {
        this(null, level);
    }

    public CacheResult(Item<T> value, byte level) {
        this.value = value;
        this.level = level;
    }

    public Item<T> getValue() {
        return value;
    }

    public byte getLevel() {
        return level;
    }

    public void setValue(Item<T> value) {
        this.value = value;
    }

    public void setLevel(byte level) {
        this.level = level;
    }
}
