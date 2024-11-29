package ru.t1.java.starter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.t1.java.starter.aspects.LogDataSourceErrorAspect;
import ru.t1.java.starter.aspects.MetricAspect;
import ru.t1.java.starter.kafka.KafkaMetricsProducer;
import ru.t1.java.starter.mapper.DataSourceErrorLogMapper;
import ru.t1.java.starter.repository.DataSourceErrorLogRepository;
import ru.t1.java.starter.service.DataSourceErrorLogService;
import ru.t1.java.starter.service.impl.DataSourceErrorLogServiceImpl;
import ru.t1.java.starter.util.DataSourceErrorLogMapperImpl;

@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "ru.t1.java.starter.repository")
public class MetricsConfig {
    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;
    private final KafkaMetricsProducer kafkaMetricsProducer;

    @Bean
    public LogDataSourceErrorAspect logDataSourceErrorAspect() {
        return new LogDataSourceErrorAspect(dataSourceErrorLogService(), kafkaMetricsProducer);
    }

    @Bean
    public MetricAspect metricAspect() {
        return new MetricAspect(kafkaMetricsProducer);
    }

    @Bean
    public DataSourceErrorLogService dataSourceErrorLogService() {
        return new DataSourceErrorLogServiceImpl(dataSourceErrorLogRepository, dataSourceErrorLogMapper());
    }

    @Bean("dataSourceErrorLogMapper")
    public DataSourceErrorLogMapper dataSourceErrorLogMapper() {
        return new DataSourceErrorLogMapperImpl();
    }
}
