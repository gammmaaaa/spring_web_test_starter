package ru.t1.java.starter.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class MessageDeserializer extends JsonDeserializer {

    private static String getMessage(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public Object deserialize(String topic, Headers headers, byte[] data) {
        try {
            return super.deserialize(topic, headers, data);
        } catch (Exception e) {
            log.warn("Произошла ошибка во время десериализации сообщения {}", new String(data, StandardCharsets.UTF_8), e);
            return null;
        }
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        try {
            return super.deserialize(topic, data);
        } catch (Exception e) {
            log.warn("Произошла ошибка во время десериализации сообщения {}",
                    new String(data, StandardCharsets.UTF_8), e);
            return null;
        }
    }
}
