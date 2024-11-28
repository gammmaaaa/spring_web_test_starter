package ru.t1.java.starter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import ru.t1.java.starter.aspects.LogDataSourceErrorAspect;
import ru.t1.java.starter.kafka.KafkaMetricsProducer;
import ru.t1.java.starter.mapper.DataSourceErrorLogMapper;
import ru.t1.java.starter.repository.DataSourceErrorLogRepository;
import ru.t1.java.starter.service.DataSourceErrorLogService;
import ru.t1.java.starter.service.impl.DataSourceErrorLogServiceImpl;

@RequiredArgsConstructor
public class MetricsConfig {
    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;
    private final KafkaMetricsProducer kafkaMetricsProducer;

    @Bean
    public LogDataSourceErrorAspect logDataSourceErrorAspect() {
        return new LogDataSourceErrorAspect(dataSourceErrorLogService(), kafkaMetricsProducer);
    }

    @Bean
    public DataSourceErrorLogService dataSourceErrorLogService() {
        return new DataSourceErrorLogServiceImpl(dataSourceErrorLogRepository, dataSourceErrorLogMapper());
    }

    @Bean("dataSourceErrorLogMapper")
    public DataSourceErrorLogMapper dataSourceErrorLogMapper() {
        return DataSourceErrorLogMapper.INSTANCE;
    }
}
