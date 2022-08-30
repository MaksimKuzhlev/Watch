package com.example.kuzhlev

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class WatchDataApplication

fun main(args: Array<String>) {
	runApplication<WatchDataApplication>(*args)
}
