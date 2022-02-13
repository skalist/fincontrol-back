package com.fincontrol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FincontrolBackApplication

fun main(args: Array<String>) {
    runApplication<FincontrolBackApplication>(*args)
}

