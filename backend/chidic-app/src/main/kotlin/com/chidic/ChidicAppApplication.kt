package com.chidic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChidicAppApplication

fun main(args: Array<String>) {
	runApplication<ChidicAppApplication>(*args)
}
