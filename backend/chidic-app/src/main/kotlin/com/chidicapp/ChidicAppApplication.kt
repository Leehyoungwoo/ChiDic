package com.chidicapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.chidicapp", "com.chidiccore", "com.chidicdomain", "com.chidiccommon"])
class ChidicAppApplication

fun main(args: Array<String>) {
	runApplication<ChidicAppApplication>(*args)
}
