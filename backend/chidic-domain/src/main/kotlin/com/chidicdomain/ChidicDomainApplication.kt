package com.chidicdomain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.domain", "com.chidicdomain"])
@EntityScan(basePackages = ["com.domain"])
@EnableJpaRepositories(basePackages = ["com.domain.repository"])
class ChidicDomainApplication

fun main(args: Array<String>) {
    runApplication<ChidicDomainApplication>(*args)
}
