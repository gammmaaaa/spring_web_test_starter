package ru.t1.java.starter.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.t1.java.starter.kafka.KafkaMetricsProducer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class MetricsKafkaConfig<T> {

    @Value("${t1.kafka.bootstrap.server}")
    private String servers;
    @Value("${t1.kafka.topic.metrics}")
    private String metricsTopic;

    @Bean("metrics")
    @ConditionalOnProperty(value = "metrics.kafka.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaTemplate<String, T> kafkaTemplate(@Qualifier("producerMetricsFactory") ProducerFactory<String, T> producerTrackFactory) {
        return new KafkaTemplate<>(producerTrackFactory);
    }

    @Bean("metricsProducer")
    @ConditionalOnProperty(value = "metrics.kafka.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaMetricsProducer producerMetrics(@Qualifier("metrics") KafkaTemplate template) {
        template.setDefaultTopic(metricsTopic);
        return new KafkaMetricsProducer(template);
    }

    @Bean("producerMetricsFactory")
    @ConditionalOnProperty(value = "metrics.kafka.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public ProducerFactory<String, T> producerMetricsFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }

}