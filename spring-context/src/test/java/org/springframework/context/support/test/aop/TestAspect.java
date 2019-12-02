package org.springframework.context.support.test.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author: Pimow
 **/
@Service
@EnableAspectJAutoProxy
@Aspect
public class TestAspect {


    @Pointcut("execution(* org.springframework.context.support.test.ioc.impl.*.*(..))")
    private void pointCutMethod() {
    }

    //声明前置通知
    @Before("pointCutMethod()")
    public void doBefore() {
        System.out.println("前置通知");
    }

//    //声明后置通知
//    @AfterReturning(pointcut = "pointCutMethod()", returning = "result")
//    public void doAfterReturning(String result) {
//        System.out.println("返回值通知");
//    }
//
//    //声明异常通知
//    @AfterThrowing(pointcut = "pointCutMethod()", throwing = "e")
//    public void doAfterThrowing(Exception e) {
//        System.out.println("声明异常通知");
//        System.out.println(e.getMessage());
//    }
//
//    //声明最终通知
//    @After("pointCutMethod()")
//    public void doAfter() {
//        System.out.println("后置通知");
//    }
//
//    //声明环绕通知
//    @Around("pointCutMethod()")
//    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
//        System.out.println("进入方法---环绕通知");
//        Object o = pjp.proceed();
//        System.out.println("退出方法---环绕通知");
//        return o;
//    }


}
