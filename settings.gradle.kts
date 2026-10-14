plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "flash-sale"

include("testkit")

include("services:order-service")
include("services:inventory-service")
