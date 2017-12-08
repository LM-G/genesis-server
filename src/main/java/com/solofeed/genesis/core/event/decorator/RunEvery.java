package com.solofeed.genesis.core.event.decorator;

import com.solofeed.genesis.core.event.UpdateEvent;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class RunEvery {

    @Pointcut(value="execution(public * com.solofeed.genesis..*(..))")
    public void anyPublicMethod() {
    }

    @Around("anyPublicMethod() && @annotation(eventListener) && @annotation(every) && args(event)")
    public void test(
        ProceedingJoinPoint joinPoint, EventListener eventListener, Every every, @NonNull UpdateEvent event)
        throws Throwable {
        if(event.getTick() % every.value() == 0){
            joinPoint.proceed();
        }
    }
}

