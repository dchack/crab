package com.crab.limiter;

import com.crab.limiter.rate.RateInfo;

/**
 * 速率控制接口
 * @author hackdc
 */
public interface RateController {

    boolean isAllow(RateInfo rateInfo);
}
