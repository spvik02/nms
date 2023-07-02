package ru.clevertec.nms.aop.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ControllerLayerLogAspect {

    @Pointcut("within(@ru.clevertec.nms.annotation.Log *)")
    private void classWithAnnotationLog() {

    }

    @Pointcut("within(ru.clevertec.nms.controller..*)")
    private void controllerClass() {

    }

    @Pointcut("execution(@ru.clevertec.nms.annotation.Log * * (..))")
    private void methodWithAnnotationLog() {

    }

    @Pointcut("execution(* ru.clevertec.nms.controller..*(..))")
    private void controllerMethod() {

    }

    @Before("(classWithAnnotationLog() || methodWithAnnotationLog()) && " +
            "(controllerClass() || controllerMethod())")
    public void logsRequest(JoinPoint joinPoint) {

        StringBuilder logMsg = new StringBuilder("Called method: ")
                .append(getCalledMethodInfo(joinPoint));

        log.debug(logMsg.toString());
    }

    @AfterReturning(
            value = "(classWithAnnotationLog() || methodWithAnnotationLog()) && " +
                    "(controllerClass() || controllerMethod())",
            returning = "returnValue"
    )
    public void logsResponse(JoinPoint joinPoint, Object returnValue) {

        StringBuilder logMsg = new StringBuilder()
                .append("Called method: ")
                .append(getCalledMethodInfo(joinPoint))
                .append("\nresponse: ")
                .append(returnValue);

        log.debug(logMsg.toString());
    }

    @AfterThrowing(
            value = "(classWithAnnotationLog() || methodWithAnnotationLog()) && " +
                    "(controllerClass() || controllerMethod())",
            throwing = "exception"
    )
    public void logsErrors(JoinPoint joinPoint, Throwable exception) {
        StringBuilder logMsg = new StringBuilder("Called method: ")
                .append(getCalledMethodInfo(joinPoint))
                .append("\nexception: ")
                .append(exception.getMessage());

        log.error(logMsg.toString());
    }

    private String getCalledMethodInfo(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName() +
                " " +
                Arrays.toString(joinPoint.getArgs()) +
                " in " +
                joinPoint.getTarget().getClass().getSimpleName();
    }
}
