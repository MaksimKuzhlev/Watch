package com.example.kuzhlev

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories



@SpringBootApplication
class WatchDataApplication
	fun main(args: Array<String>) {
		runApplication<WatchDataApplication>(*args)
	}
