package com.flashsale.testkit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public abstract class InfrastructureSmokeTest extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private KafkaTemplate<String, String> kafka;

    @Test
    void flywayAppliedMigrations() {
        List<String> tables = jdbc.queryForList(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'",
                String.class);

        assertThat(tables).contains("outbox", "processed_events", "flyway_schema_history");
    }

    @Test
    void kafkaRoundTrip() throws Exception {
        String topic = "smoke-" + UUID.randomUUID();
        kafka.send(topic, "key", "hello").get(10, TimeUnit.SECONDS);

        Map<String, Object> props = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers(),
                ConsumerConfig.GROUP_ID_CONFIG, "smoke-" + UUID.randomUUID(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        try (var consumer = new KafkaConsumer<String, String>(props)) {
            consumer.subscribe(List.of(topic));
            List<ConsumerRecord<String, String>> received = new ArrayList<>();

            await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
                consumer.poll(Duration.ofMillis(200)).forEach(received::add);
                assertThat(received).hasSize(1);
            });

            assertThat(received.get(0).key()).isEqualTo("key");
            assertThat(received.get(0).value()).isEqualTo("hello");
        }
    }
}
