package com.crab.cache.multi.metric;

import com.crab.cache.multi.CacheResult;

import java.util.concurrent.atomic.LongAdder;

/**
 * cache stats
 *
 * @author hackdc
 * @Date 2022/7/30 3:20 PM
 */
public class MultiCacheStats {

    private LongAdder l1HitCounter = new LongAdder();
    private LongAdder l2HitCounter = new LongAdder();
    private LongAdder missCounter = new LongAdder();

    public void recordL1Hit(int hit) {
        l1HitCounter.add(hit);
    }

    public void recordL2Hit(int hit) {
        l2HitCounter.add(hit);
    }

    public void recordMiss(int miss) {
        missCounter.add(miss);
    }

    public void recordL1Hit() {
        l1HitCounter.add(1);
    }

    public void recordL2Hit() {
        l2HitCounter.add(1);
    }

    public void recordMiss() {
        missCounter.add(1);
    }

    public long getL1HitCounter() {
        return l1HitCounter.longValue();
    }

    public long getL2HitCounter() {
        return l2HitCounter.longValue();
    }

    public long getMissCounter() {
        return missCounter.longValue();
    }

    public void record(byte level){
        switch (level) {
            case CacheResult.LEVEL_1 :
                recordL1Hit();
                break;
            case CacheResult.LEVEL_2:
                recordL2Hit();
                break;
            case CacheResult.LEVEL_OUTER:
                recordMiss();
                break;
        }
    }
}
