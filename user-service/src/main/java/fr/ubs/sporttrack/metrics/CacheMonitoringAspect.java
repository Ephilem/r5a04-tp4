package fr.ubs.sporttrack.metrics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheMonitoringAspect {
    private static final Logger logger = LoggerFactory.getLogger(CacheMonitoringAspect.class);

    @Around("@annotation(org.springframework.cache.annotation.Cacheable)")
    public Object monitorCacheMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            
            logger.info("Method: {} - Execution Time: {}ms", methodName, executionTime);
            return result;
        } catch (Exception e) {
            logger.error("Cache error in method: {} - Error: {}", methodName, e.getMessage());
            throw e;
        }
    }
}