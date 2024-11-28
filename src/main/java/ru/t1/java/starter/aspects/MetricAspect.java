package ru.t1.java.starter.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.java.starter.annotations.Metric;
import ru.t1.java.starter.dto.ExecutionTimeMetric;
import ru.t1.java.starter.kafka.KafkaMetricsProducer;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {
    @Value("${t1.kafka.topic.metrics}")
    private String metricsTopic;
    private final KafkaMetricsProducer kafkaMetricsProducer;

    @Around("@within(ru.t1.java.starter.annotations.Metric)")
    public Object logExecTime(ProceedingJoinPoint pJoinPoint) throws Throwable {
        log.info("Вызов метода: {}", pJoinPoint.getSignature().toShortString());
        long timeForExecute = pJoinPoint.getTarget().getClass().getAnnotation(Metric.class).time();
        long beforeTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = pJoinPoint.proceed();
        } finally {
            long executeTime = System.currentTimeMillis() - beforeTime;
            log.info("Время исполнения: {} ms", executeTime);
            if (executeTime > timeForExecute) {
                ExecutionTimeMetric executionTimeMetric = ExecutionTimeMetric.builder()
                        .executionTime(executeTime)
                        .methodSignature(pJoinPoint.getSignature().getName())
                        .args(Arrays.stream(pJoinPoint.getArgs()).toList())
                        .build();
                kafkaMetricsProducer.sendToWithHeader(metricsTopic, executionTimeMetric, "METRICS");
            }
        }

        return result;
    }
}
