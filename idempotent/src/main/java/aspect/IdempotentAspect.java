package aspect;

import annotation.Idempotent;
import context.IdempotentContextHolder;
import exception.IdempotentException;
import executor.IdempotentExecutor;
import executor.IdempotentRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/8/24 8:02 PM
 */
@Aspect
public class IdempotentAspect {

    private IdempotentExecutor idempotentExecutor;

    public IdempotentAspect(IdempotentExecutor idempotentExecutor) {
        this.idempotentExecutor = idempotentExecutor;
    }

    @Around(value = "@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinpoint, Idempotent idempotent) throws Throwable {
        Object[] args = joinpoint.getArgs();
        Method method = ((MethodSignature) joinpoint.getSignature()).getMethod();
        long expire = idempotent.expire();

        String key;
        if (StringUtils.hasText(idempotent.key())) {
            key = parseKey(idempotent.key(), method, args);
        } else {
            key = IdempotentContextHolder.getContext().get("idempotentId");
        }

        String userInputKey = idempotent.name();
        if (!StringUtils.hasText(userInputKey)) {
            userInputKey = method.getName();
        }
        String idempotentKey = userInputKey + ":" + key;

        IdempotentRequest request = new IdempotentRequest().setKey(idempotentKey)
                .setExpire(expire);


        return idempotentExecutor.execute(request, () -> {
            try {
                return joinpoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            throw new IdempotentException("Repeated requests");
        });

    }


    /**
     * 获取幂等的key, 支持SPEL表达式
     * @param key
     * @param method
     * @param args
     * @return
     */
    private String parseKey(String key, Method method, Object[] args){
        LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = nameDiscoverer.getParameterNames(method);
        if (paraNameArr == null || paraNameArr.length <= 0) {
            throw new RuntimeException("SPEL parse error, the target method cannot be a no-parameter method");
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
