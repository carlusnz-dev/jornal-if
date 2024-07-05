package br.com.gremiorupestre.grer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrerApplication

fun main(args: Array<String>) {
    runApplication<GrerApplication>(*args)
}
