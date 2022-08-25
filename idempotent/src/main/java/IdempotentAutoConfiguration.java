import aspect.IdempotentAspect;
import executor.DefaultIdempotentExecutor;
import executor.IdempotentExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import storage.IdempotentRecordStorage;
import storage.oracle.OracleStorage;

/**
 *
 * @author hackdc
 * @Date 2022/8/24 9:05 PM
 */
@Configuration
public class IdempotentAutoConfiguration {

    @Bean
    @ConditionalOnBean(IdempotentRecordStorage.class)
    public IdempotentExecutor idempotentExecutor(IdempotentRecordStorage idempotentRecordStorage) {
        return new DefaultIdempotentExecutor(idempotentRecordStorage);
    }

    @Bean
    @ConditionalOnBean(JdbcTemplate.class)
    public OracleStorage oracleStorage(JdbcTemplate jdbcTemplate) {
        return new OracleStorage(jdbcTemplate);
    }

    @Bean
    @ConditionalOnBean(IdempotentExecutor.class)
    IdempotentAspect idempotentAspect(IdempotentExecutor idempotentExecutor) {
        return new IdempotentAspect(idempotentExecutor);
    }

}
