plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25" apply false
    kotlin("plugin.jpa") version "1.9.25" apply false
    id("org.springframework.boot") version "3.4.1" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    kotlin("kapt") version "1.7.10" apply false
}

allprojects {
    group = "com"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    java.sourceCompatibility = JavaVersion.VERSION_21
    java.targetCompatibility = JavaVersion.VERSION_21

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.boot:spring-boot-starter-web")
        testImplementation(kotlin("test"))
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

        testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        // JUnit 5: 최신 버전으로 JUnit 5를 사용할 수 있게 해주는 의존성
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
        testImplementation(kotlin("test"))
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.register("prepareKotlinBuildScriptModel"){

    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }
}
