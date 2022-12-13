package com.yf.document.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 日志过滤
 * @author bool
 */
@Aspect
@Component
public class LogAspect {


//    @Autowired
//    private BaseLogAspect baseLogAspect;
//
//
//    /**
//     * 针对注解过的方法，进行保存日志
//     * @param jp
//     * @return
//     * @throws Throwable
//     */
//    @Around("@annotation(com.yf.boot.base.api.annon.LogInject)")
//    public Object doAround(ProceedingJoinPoint jp) throws Throwable {
//       return baseLogAspect.record(jp);
//    }


}
