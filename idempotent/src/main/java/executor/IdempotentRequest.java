package executor;

/**
 * TODO
 *
 * @author hackdc
 * @Date 2022/8/24 8:22 PM
 */
public class IdempotentRequest {

    private String key;

    private long expire;

    public IdempotentRequest setKey(String key) {
        this.key = key;
        return this;
    }

    public IdempotentRequest setExpire(long expire) {
        this.expire = expire;
        return this;
    }

    public String getKey() {
        return key;
    }

    public long getExpire() {
        return expire;
    }
}
