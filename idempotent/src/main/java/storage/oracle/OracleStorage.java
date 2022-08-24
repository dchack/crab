package storage.oracle;

import org.springframework.jdbc.core.JdbcTemplate;
import storage.IdempotentRecordStorage;

import java.util.Date;

/**
 * Oracle impl
 *
 * @author dongchao
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
        String sql = "insert into IDEMPOTENT_RECORD(`KEY`, CREATE_TIME, EXPIRE_TIME) values(?,?,?,?)";
        jdbcTemplate.update(sql, key, new Date(), expireDate);
    }

    @Override
    public boolean hasKey(String key) {
        String sql = "select count(1) from IDEMPOTENT_RECORD WHERE KEY = " + key;
        Integer value = jdbcTemplate.queryForObject(sql, Integer.class, key);
        return value > 0;
    }

}
