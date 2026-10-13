package com.flashsale.testkit;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

/**
 * Basic integration test class
 *
 * <p>Containers are singletons for the entire JVM of the test run: they start once, not for each test class.
 *
 * <p>Isolation between tests is the responsibility of the tests themselves (unique topics/keys,
 *  table cleanup), not the recreation of containers.
 */
public abstract class AbstractIntegrationTest {

    protected static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    protected static final KafkaContainer KAFKA =
            new KafkaContainer(DockerImageName.parse("apache/kafka:3.8.0"));

    static {
        Startables.deepStart(POSTGRES, KAFKA).join();
    }

    @DynamicPropertySource
    static void infrastructureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }
}
