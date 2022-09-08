import rate.RateInfo;

/**
 * TODO
 *
 * @author hackdc
 * @Date 2022/9/8 2:10 PM
 */
public class RedisWindowRateController implements RateController{
    @Override
    public boolean isAllow(RateInfo rateInfo) {
        return false;
    }
}
