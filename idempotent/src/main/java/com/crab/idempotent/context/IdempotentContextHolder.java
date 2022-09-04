package com.crab.idempotent.context;


/**
 *
 * @author hackdc
 * @Date 2022/8/24 8:53 PM
 */
public class IdempotentContextHolder {

    private static final ThreadLocal<IdempotentContext> contextHolder = ThreadLocal.withInitial(IdempotentContext::new);

    public static IdempotentContext getContext() {
        return contextHolder.get();
    }

    public static void removeContext() {
        contextHolder.remove();
    }

    public static void setContext(IdempotentContext context) {
        contextHolder.set(context);
    }

}
