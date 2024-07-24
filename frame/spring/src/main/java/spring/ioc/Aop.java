package spring.ioc;

import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Log
@Aspect
public class Aop {

    @Pointcut("execution(* spring.ioc.*+.*(..))")
    public void logger(){}

    @Before("logger()")
    public void before(JoinPoint joinPoint) {
        log.info(joinPoint.getTarget().getClass()+" before:"+joinPoint);
    }

    @After("logger()")
    public void atfer(JoinPoint joinPoint){
        log.info(joinPoint.getTarget().getClass()+" after:" + joinPoint);
    }

    @Around("logger()")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info(proceedingJoinPoint.getTarget().getClass()+" around before:"+proceedingJoinPoint);
        proceedingJoinPoint.proceed();
        log.info(proceedingJoinPoint.getTarget().getClass()+" around after:"+proceedingJoinPoint);
    }

}
