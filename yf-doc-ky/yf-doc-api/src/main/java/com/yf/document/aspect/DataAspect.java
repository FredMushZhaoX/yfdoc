package com.yf.document.aspect;

import com.yf.boot.base.aspect.data.BaseDataAspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数据保护AOP，保护系统数据不被删除
 * @author bool
 */
@Aspect
@Component
@Slf4j
public class DataAspect {

    @Autowired
    private BaseDataAspect baseDataAspect;

    /**
     * 切入Controller执行
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.yf.boot.base.api.annon.DataProtect)")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return baseDataAspect.protect(pjp);
    }
}
