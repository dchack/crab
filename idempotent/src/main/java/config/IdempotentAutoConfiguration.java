package config;

import aspect.IdempotentAspect;
import executor.IdempotentExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import storage.IdempotentRecordStorage;
import storage.StorageFactory;
import storage.oracle.OracleStorage;
import storage.redis.RedisStorage;

import java.util.List;

/**
 *
 * @author hackdc
 * @Date 2022/8/24 9:05 PM
 */
@Configuration
public class IdempotentAutoConfiguration {

    private JdbcTemplate jdbcTemplate;

    public IdempotentAutoConfiguration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Bean
//    @ConditionalOnBean({RedisLockService.class})
//    public IdempotentExecutor idempotentExecutor(RedisLockService redisLockService, StorageFactory storageFactory) {
//        return new DefaultIdempotentExecutor(redisLockService, storageFactory);
//    }

    @Bean
    @ConditionalOnClass(oracle.jdbc.OracleConnection.class)
    public OracleStorage oracleStorage() {
        return new OracleStorage(jdbcTemplate);
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisStorage redisStorage(RedisTemplate redisTemplate) {
        return new RedisStorage(redisTemplate);
    }

    @Bean
    @ConditionalOnBean(IdempotentExecutor.class)
    public IdempotentAspect idempotentAspect(IdempotentExecutor idempotentExecutor) {
        return new IdempotentAspect(idempotentExecutor);
    }

    @Bean
    @ConditionalOnBean(IdempotentRecordStorage.class)
    public StorageFactory storageFactory(List<IdempotentRecordStorage> list) {
        return new StorageFactory(list);
    }

}
