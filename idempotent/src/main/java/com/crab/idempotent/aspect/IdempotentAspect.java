package com.crab.idempotent.aspect;

import com.crab.idempotent.annotation.Idempotent;
import com.crab.idempotent.exception.IdempotentException;
import com.crab.idempotent.executor.IdempotentRequest;
import com.crab.idempotent.context.IdempotentContextHolder;
import com.crab.idempotent.executor.IdempotentExecutor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * Idempotent Aspect
 *
 * @author hackdc
 * @Date 2022/8/24 8:02 PM
 */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class IdempotentAspect {

    private IdempotentExecutor idempotentExecutor;

    public IdempotentAspect(IdempotentExecutor idempotentExecutor) {
        this.idempotentExecutor = idempotentExecutor;
    }

    @Pointcut("@annotation(com.crab.idempotent.annotation.Idempotent)")
    public void pointCut() {

    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint joinpoint) {
        Object[] args = joinpoint.getArgs();
        Method method = ((MethodSignature) joinpoint.getSignature()).getMethod();
        Idempotent idempotentAnnotation = method.getAnnotation(Idempotent.class);
        long idempotentExpire = idempotentAnnotation.idempotentExpire();
        long lockExpire = idempotentAnnotation.lockExpire();

        String key;
        if (StringUtils.hasText(idempotentAnnotation.key())) {
            key = parseKey(idempotentAnnotation.key(), method, args);
        } else {
            key = IdempotentContextHolder.getContext().get("idempotentId");
        }

        if(key == null) {
            throw new RuntimeException("idempotent key is null");
        }

        String userInputKey = idempotentAnnotation.name();
        if (!StringUtils.hasText(userInputKey)) {
            userInputKey = method.getName();
        }
        String idempotentKey = userInputKey + ":" + key;

        IdempotentRequest request = new IdempotentRequest().setKey(idempotentKey)
                .setIdempotentExpire(idempotentExpire).setLockExpire(lockExpire);

        return idempotentExecutor.execute(request, () -> {
            try {
                return joinpoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            throw new IdempotentException("重复请求");
        });

    }

    /**
     * parse key for SPEL
     * @param key
     * @param method
     * @param args
     * @return
     */
    private String parseKey(String key, Method method, Object[] args){
        LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = nameDiscoverer.getParameterNames(method);
        if (paraNameArr == null || paraNameArr.length <= 0) {
            // Parameters must not be none
            return null;
        }
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        for(int i = 0; i < paraNameArr.length; i++){
            context.setVariable(paraNameArr[i], args[i]);
        }
        try {
            return parser.parseExpression(key).getValue(context, String.class);
        } catch (SpelEvaluationException e) {
            throw new RuntimeException("SPEL parse error", e);
        }
    }
}

