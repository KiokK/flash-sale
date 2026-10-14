import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}

val springBootVersion = libs.versions.spring.boot.get()

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    group = "com.flashsale"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    extra["testcontainers.version"] = "1.21.4"

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    extensions.configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("failed", "skipped")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}
