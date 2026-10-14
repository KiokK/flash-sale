plugins {
    `java-library`
}

dependencies {
    api("org.springframework:spring-test")
    api("org.testcontainers:postgresql")
    api("org.testcontainers:kafka")
    api("org.awaitility:awaitility")
    api(libs.archunit)

    api("org.springframework:spring-jdbc")
    api("org.springframework.kafka:spring-kafka")
    api("org.springframework.boot:spring-boot-starter-test")
}
