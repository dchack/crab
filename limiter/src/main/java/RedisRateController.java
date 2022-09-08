import rate.RateInfo;


/**
 * 漏桶算法速率控制器
 * @author hackdc
 */
public class RedisRateController implements RateController {

    @Override
    public boolean isAllow(RateInfo rateInfo) {
        return false;
    }

}
