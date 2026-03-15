package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Slf4j
@Component
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)" )
    public void autoFillPointcut(){}


    @Before(value = "autoFillPointcut()" )
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段数据自动填充");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获取方法签名对象
        AutoFill autoFill = signature.getMethod().getDeclaredAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType operationType = autoFill.value();//获取数据库操作类型

        Object[] args = joinPoint.getArgs();//获取当前被拦截方法的参数
        if (args == null || args.length == 0){//如果参数为空或参数数量为0，直接返回
            return;
        }
        Object entity = args[0];//获取实体对象

        LocalDateTime now = LocalDateTime.now();//获取当前时间
        Long id = BaseContext.getCurrentId();//获取当前的用户id

        try {
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
            setUpdateTime.invoke(entity, now);
            setUpdateUser.invoke(entity, id);
            if (operationType == OperationType.INSERT){//当是插入操作是，才会设置创建时间和创建用户id
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                setCreateUser.invoke(entity, id);
                setCreateTime.invoke(entity, now);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
