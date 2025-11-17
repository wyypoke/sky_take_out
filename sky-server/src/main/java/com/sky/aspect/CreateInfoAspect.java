package com.sky.aspect;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class CreateInfoAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.CreateInfo)")
    public void createInfoPointCut() {}
    @Before("createInfoPointCut()")
    public void createInfo(JoinPoint joinPoint) {
        log.info("开始填充创建信息");
        Object[] args = joinPoint.getArgs();
        Object entity = args[0];
        Class<?> entityClass = entity.getClass();

        try {
            // 获取 setCreateTime 方法的定义
            Method setCreateTimeMethod = entityClass.getMethod("setCreateTime", LocalDateTime.class);

            // 调用 setCreateTime 方法
            setCreateTimeMethod.invoke(entity, LocalDateTime.now());

        } catch (NoSuchMethodException e) {
            log.info("没有setCreateTime方法");
        } catch (Exception e) {

        }

        try {
            Long id = JwtTokenAdminInterceptor.get();
            Method setCreateUserMethod = entityClass.getMethod("setCreateUser", Long.class);

            // 调用 setCreateUser 方法
            setCreateUserMethod.invoke(entity, id);

        } catch (NoSuchMethodException e) {
            log.info("没有setCreateUser方法");
        } catch (Exception e) {

        }
    }

}
