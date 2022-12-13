package com.yf.document.aspect;

import com.yf.boot.base.aspect.dict.BaseDictAspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数据字典AOP类，处理数据字典值
 *
 * @author bool
 */
@Aspect
@Component
@Slf4j
public class DictAspect {

    @Autowired
    private BaseDictAspect baseDictAspect;

    /**
     * 切入Controller执行
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(public *  com.yf.document..*.*Controller.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return baseDictAspect.translate(pjp);
    }


}
