package storage;

/**
 * 幂等数据存储层
 *
 * @author dongchao
 * @Date 2022/8/24 7:04 PM
 */
public interface IdempotentRecordStorage {

    /**
     * 永久幂等
     * @param key
     */
    void setKey(String key);

    /**
     * 有过期时间的幂等
     * @param key
     * @param expire 秒
     */
    void setKey(String key, long expire);

    /**
     * 检查业务是否执行完成
     * 存在key 表示已经执行完成
     * @param key
     * @return
     */
    boolean hasKey(String key);
}
