package storage.oracle;

import org.springframework.jdbc.core.JdbcTemplate;
import storage.IdempotentRecordStorage;
import storage.StorageTypeEnum;

import java.util.Date;

/**
 * Oracle impl
 *
 * @author hackdc
 * @Date 2022/8/24 7:27 PM
 */
public class OracleStorage implements IdempotentRecordStorage {

    private JdbcTemplate jdbcTemplate;

    public OracleStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setKey(String key) {
        setKey(key,0);
    }

    @Override
    public void setKey(String key, long expire) {
        Date expireDate = expire == 0 ? null : new Date(System.currentTimeMillis() + expire * 1000);
        String sql = "insert into AAP_IDEMPOTENT_RECORD(ID, KEY, CREATE_TIME, EXPIRE_TIME) values(IDEMPOTENT_RECORD_SEQUENCE.nextval, ?,?,?)";
        jdbcTemplate.update(sql, key, new Date(), expireDate);
    }

    @Override
    public boolean hasKey(String key) {
        String sql = "select count(1) from AAP_IDEMPOTENT_RECORD WHERE KEY = ?";
        Integer value = jdbcTemplate.queryForObject(sql, Integer.class, key);
        return value > 0;
    }

    @Override
    public StorageTypeEnum getType() {
        return StorageTypeEnum.ORACLE;
    }

}

