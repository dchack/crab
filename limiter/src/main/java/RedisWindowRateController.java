import org.springframework.data.redis.core.RedisTemplate;
import rate.RateInfo;

import java.util.concurrent.TimeUnit;

/**
 * Fixed window limit by redis
 *
 * @author hackdc
 * @Date 2022/9/8 2:10 PM
 */
public class RedisWindowRateController implements RateController{

    private RedisTemplate<String, String> redisTemplate;

    private static final String LIMIT_KEY = "limit:key";

    public RedisWindowRateController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllow(RateInfo rateInfo) {
        String limitKey = LIMIT_KEY + rateInfo.getName();
        long expireSeconds = rateInfo.getSecondByUnit();
        String value = redisTemplate.opsForValue().get(limitKey);
        if(value != null) {
            long last = Long.parseLong(value);
            if(last >= rateInfo.getRate()){
                return false;
            }
            redisTemplate.opsForValue().increment(limitKey, 1);
        } else {
            redisTemplate.opsForValue().increment(limitKey, 1);
            redisTemplate.expire(limitKey, expireSeconds, TimeUnit.SECONDS);
        }
        return true;
    }
}
