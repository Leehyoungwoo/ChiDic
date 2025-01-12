package com.chidicdomain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan(basePackages = ["com.domain"])
class ChidicDomainApplication

fun main(args: Array<String>) {
    runApplication<ChidicDomainApplication>(*args)
}
