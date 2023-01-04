package com.crab.test.cache;

import com.alibaba.csp.sentinel.slots.statistic.data.MetricBucket;
import com.alibaba.csp.sentinel.slots.statistic.metric.ArrayMetric;
import com.alibaba.csp.sentinel.slots.statistic.metric.Metric;
import com.alibaba.csp.sentinel.slots.statistic.metric.occupy.OccupiableBucketLeapArray;

import java.util.List;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2023/1/4 5:19 PM
 */
public class LeapArrayTest {

    private transient static Metric rollingCounterInMinute = new ArrayMetric(60, 60 * 1000, false);


    public static void main(String[] args) {

        OccupiableBucketLeapArray data = new OccupiableBucketLeapArray(2, 10000);

        for (int i = 0; i < 10; i++) {
            data.currentWindow();
            long pass = 0;
            List<MetricBucket> list = data.values();

            for (MetricBucket window : list) {
                pass += window.pass();
            }
            if (pass > 0) {
                System.out.printf("test leapArray block exception");
            }

        }

        for (int i = 0; i < 10; i++) {
            rollingCounterInMinute.addPass(1);
            long pass = rollingCounterInMinute.pass();
            if (pass > 2) {
                System.out.println("test leapArray block exception");
            }
        }
    }
}
