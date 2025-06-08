import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	kotlin("plugin.spring")
	id("org.springframework.boot")
	id("io.spring.dependency-management")
}

tasks.getByName<Jar>("jar") {
	enabled = true
}

tasks.getByName<BootJar>("bootJar") {
	enabled = false
}

dependencies {
	api("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation(project(":chidic-common"))
}
