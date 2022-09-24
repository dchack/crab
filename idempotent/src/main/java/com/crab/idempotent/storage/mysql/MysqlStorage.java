package com.crab.idempotent.storage.mysql;

import com.crab.idempotent.storage.IdempotentRecordStorage;
import com.crab.idempotent.storage.StorageTypeEnum;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

/**
 * Mysql impl
 *
 * @author dongchao
 * @Date 2022/9/24 10:14 PM
 */
public class MysqlStorage implements IdempotentRecordStorage {

    private JdbcTemplate jdbcTemplate;

    public MysqlStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setKey(String key, long expire) {
        Date expireDate = expire == 0 ? null : new Date(System.currentTimeMillis() + expire * 1000);
        String sql = "insert into IDEMPOTENT_RECORD(KEY, CREATE_TIME, EXPIRE_TIME) values(?,?,?)";
        jdbcTemplate.update(sql, key, new Date(), expireDate);
    }

    @Override
    public boolean hasKey(String key) {
        String sql = "select count(1) from IDEMPOTENT_RECORD WHERE KEY = ?";
        Integer value = jdbcTemplate.queryForObject(sql, Integer.class, key);
        return value > 0;
    }

    @Override
    public StorageTypeEnum getType() {
        return StorageTypeEnum.MYSQL;
    }
}
