package what.the.oauth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WhatTheOauthApplication

fun main(args: Array<String>) {
    runApplication<WhatTheOauthApplication>(*args)

    println("\n\n* * * WhatTheOauthApplication start!!! * * * \n")
}
