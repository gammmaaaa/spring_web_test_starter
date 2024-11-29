package ru.t1.java.starter.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.java.starter.dto.DataSourceErrorLogDto;
import ru.t1.java.starter.service.DataSourceErrorLogService;
import ru.t1.java.starter.kafka.KafkaMetricsProducer;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogDataSourceErrorAspect {
    private final DataSourceErrorLogService dataSourceErrorLogService;
    private final KafkaMetricsProducer kafkaMetricsProducer;

    @Value("${t1.kafka.topic.metrics}")
    private String metricsTopic;

    @AfterThrowing(pointcut = "@within(ru.t1.java.starter.annotations.LogDataSourceError)",
            throwing = "e")
    public void afterThrowingCRUDErrorAdvice(JoinPoint joinPoint, Exception e) {
        log.info("AFTER THROWING IN: {}", joinPoint.getSignature().getName());
        DataSourceErrorLogDto dataSourceErrorLogDto = DataSourceErrorLogDto.builder()
                .message(e.getMessage())
                .stacktrace(ExceptionUtils.getStackTrace(e))
                .methodSignature(joinPoint.getSignature().getName())
                .build();
        try {
            kafkaMetricsProducer.sendToWithHeader(metricsTopic, dataSourceErrorLogDto, "DATA_SOURCE");
        } catch (Exception exception) {
            log.error("DATA SOURCE KAFKA ERROR: {}", exception.getMessage());
            dataSourceErrorLogService.saveDataSourceError(dataSourceErrorLogDto, exception.getMessage());
        }
    }


}
