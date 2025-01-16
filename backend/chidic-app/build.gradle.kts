plugins {
	kotlin("plugin.spring")
	id("org.springframework.boot")
	id("io.spring.dependency-management")
}

dependencies {
	implementation(project(":chidic-core"))
	implementation(project(":chidic-domain"))
	implementation(project(":chidic-common"))
}