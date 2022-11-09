package liga.medical.medicalmonitoring.core.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAdvice {

    @Pointcut(value = "execution(* liga.medical.medicalmonitoring.core.listener.*.*(..))")
    public void listenerPointcut() {
    }

    @Around("listenerPointcut()")
    public Object listenerAppLogger(ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();

        Object[] array = joinPoint.getArgs();

        log.info("В методе " + methodName + "() класса " + className + " обрабатывается сообщение :" + array.toString());

        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        log.info("Сообщение было обработано.");
        return object;
    }

    @Pointcut(value = "execution(* liga.medical.medicalmonitoring.core.api.RabbitSenderService.sendMessage(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object appLogger(ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();

        log.info("Метод " + methodName + "() класса " + className + " определяет тип сообщения");

        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        log.info("Сообщение класифицированно : " + mapper.writeValueAsString(object));
        return object;
    }

    @Pointcut(value = "execution(* liga.medical.medicalmonitoring.core.api.RabbitSenderService.sendError(..))")
    public void pointcutWithError() {
    }

    @Around("pointcutWithError()")
    public Object appLoggerWithError(ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();

        log.info("При классификации сообщения в методе " + methodName + "() класса " + className + " возникла ошибка");

        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        log.info("Сообщение класифицированно : " + mapper.writeValueAsString(object));
        return object;
    }
}
