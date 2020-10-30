package com.project.najdiprevoz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@ComponentScan(value = ["com.project"])
@SpringBootApplication
@EnableScheduling
class NajdiprevozApplication

fun main(args: Array<String>) {
    runApplication<NajdiprevozApplication>(*args)
}
