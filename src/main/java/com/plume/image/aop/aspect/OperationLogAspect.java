package com.plume.image.aop.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.server.HttpServerBase;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.plume.image.annotation.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    @Pointcut("@annotation(com.plume.image.annotation.OperationLog)")
    public void pt() {
    }

    @Around("pt()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long time = System.currentTimeMillis() - beginTime;

        // System.out.println("操作耗时"+time+"ms");
        recordLog(joinPoint,time);

        return result;
    }
    @AfterThrowing(pointcut ="@annotation(operationLog)",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint,OperationLog operationLog,Exception e){
        log.error("异常请求为:{}:{}",operationLog.moudle(),operationLog.operator());
        log.error("异常信息:{}",e.getMessage());
    }


    private void recordLog(ProceedingJoinPoint joinPoint,long time){
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog logAnnotation = method.getAnnotation(OperationLog.class);
        log.info("=====================log start=======================");
        log.info("module:{}",logAnnotation.moudle());
        log.info("operation:{}",logAnnotation.operator());

        // 请求方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method:{}",className+"."+methodName+"()");


        String params = null;
        // 请求的参数
        Object[] args = joinPoint.getArgs();

        if (args.length != 0){
            params = JSONUtil.toJsonStr(args[0]);
        }

        log.info("param:{}",params);

        log.info("execute time: {} ms",time);
        log.info("=====================log end=======================");
    }
}

