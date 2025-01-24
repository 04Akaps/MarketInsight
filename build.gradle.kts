plugins {
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("plugin.spring") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    kotlin("jvm") version "2.0.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    // validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // json
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // ktor
    implementation("io.ktor:ktor-client-core:2.2.3") // 최신 2.x 버전
    implementation("io.ktor:ktor-client-cio:2.2.3")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // lombok
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    // serializer
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    // logging
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.10")

    // spring
    implementation("org.springframework.boot:spring-boot-starter")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // mongo
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // 코루틴
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}