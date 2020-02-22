package com.project.najdiprevoz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class NajdiprevozApplication

fun main(args: Array<String>) {
    runApplication<NajdiprevozApplication>(*args)
}
