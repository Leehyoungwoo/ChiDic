plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("kapt")
}

dependencies {
    implementation(project(":chidic-common"))
    api("org.springframework.kafka:spring-kafka")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.jetbrains.kotlin:kotlin-reflect")

    // MapStruct 의존성
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")
    runtimeOnly("com.mysql:mysql-connector-j")

    // 테스트를 위한 의존성
    api("org.springframework.boot:spring-boot-starter-test")
    api("com.h2database:h2")

    // 레디스 의존성
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

    // 직렬화 라이브러리
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.0")

    // 로컬 캐시
    implementation("com.google.guava:guava:30.1-jre")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
