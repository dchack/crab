package com.crab.cache.multi.metric;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.BaseUnits;
import io.micrometer.core.instrument.binder.MeterBinder;

/**
 *  metric for multi cache
 *
 * @author hackdc
 * @Date 2022/7/30 10:05 PM
 */
public class MultiCacheMetric implements MeterBinder {

    private final Iterable<Tag> tags;
    private final MultiCacheStats multiCacheStats;

    public MultiCacheMetric(Iterable<Tag> tags, MultiCacheStats multiCacheStats) {
        this.tags = tags;
        this.multiCacheStats = multiCacheStats;
    }

    @Override
    public void bindTo(MeterRegistry registry) {

        Gauge.builder("cache.first.hit", multiCacheStats, MultiCacheStats::getL1HitCounter)
                .tags(tags)
                .description("The number of first cache hits")
                .baseUnit(BaseUnits.TASKS)
                .register(registry);

        Gauge.builder("cache.second.hit", multiCacheStats, MultiCacheStats::getL2HitCounter)
                .tags(tags)
                .description("The number of second cache hits")
                .baseUnit(BaseUnits.TASKS)
                .register(registry);

        Gauge.builder("cache.all.miss", multiCacheStats, MultiCacheStats::getMissCounter)
                .tags(tags)
                .description("The number of all cache miss")
                .baseUnit(BaseUnits.TASKS)
                .register(registry);
    }
}
