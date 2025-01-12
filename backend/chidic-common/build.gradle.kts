plugins {
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
