package context;

import java.util.HashMap;
import java.util.Map;

/**
 * idempotent context
 * It must be removed after use
 *
 * @author hackdc
 * @Date 2022/8/24 8:55 PM
 */
public class IdempotentContext {

    private final Map<String, String> attributes = new HashMap<String, String>();


    public void put(String key, String value) {
        attributes.put(key, value);
    }

    public void remove(String key) {
        attributes.remove(key);
    }

    public String get(String key) {
        return attributes.get(key);
    }

}
