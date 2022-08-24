package annotation;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/8/24 12:05 AM
 */
public @interface Idempotent {


    /**
     * Name used to determine the target idempotent key prefix
     *
     * @return
     */
    String name();

    /**
     * support Spring Expression Language (SpEL)
     *
     * @return
     */
    String key();

    /**
     * set key expire time, second unit
     * @return
     */
    long expire();

}
