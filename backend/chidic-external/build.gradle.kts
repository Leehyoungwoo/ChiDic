plugins {
	kotlin("plugin.spring")
	id("org.springframework.boot")
	id("io.spring.dependency-management")
}

dependencies {
	api("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation(project(":chidic-common"))
}
