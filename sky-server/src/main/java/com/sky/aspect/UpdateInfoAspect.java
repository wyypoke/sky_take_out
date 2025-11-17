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
public class UpdateInfoAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.UpdateInfo)")
    public void updateInfoPointCut() {}


    @Before("updateInfoPointCut()")
    public void updateInfo(JoinPoint joinPoint) {
        log.info("开始填充更新信息"); // 日志信息修改

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0 || args[0] == null) {
            log.warn("无法填充更新信息：目标方法参数为空或第一个参数为null。");
            return;
        }

        Object entity = args[0];
        Class<?> entityClass = entity.getClass();

        // ----------------------------------------------------
        // 1. 反射调用 setUpdateTime(LocalDateTime.now())
        // ----------------------------------------------------
        try {
            // 方法名 setUpdateTime
            Method setUpdateTimeMethod = entityClass.getMethod("setUpdateTime", LocalDateTime.class);

            // 调用 setUpdateTime 方法
            setUpdateTimeMethod.invoke(entity, LocalDateTime.now());

        } catch (NoSuchMethodException e) {
            log.info("实体类 {} 没有 setUpdateTime(LocalDateTime) 方法", entityClass.getSimpleName());
        } catch (Exception e) {
            log.error("反射调用 setUpdateTime 失败: {}", e.getMessage(), e);
        }

        // ----------------------------------------------------
        // 2. 反射调用 setUpdateUser(JwtTokenAdminInterceptor.get())
        // ----------------------------------------------------
        try {
            Long id = JwtTokenAdminInterceptor.get();

            // 方法名 setUpdateUser
            Method setUpdateUserMethod = entityClass.getMethod("setUpdateUser", Long.class);

            // 调用 setUpdateUser 方法
            setUpdateUserMethod.invoke(entity, id);

        } catch (NoSuchMethodException e) {
            log.info("实体类 {} 没有 setUpdateUser(Long) 方法", entityClass.getSimpleName());
        } catch (Exception e) {
            log.error("反射调用 setUpdateUser 失败: {}", e.getMessage(), e);
        }
    }
}