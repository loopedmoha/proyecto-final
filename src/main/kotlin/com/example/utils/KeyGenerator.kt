package com.example.utils

import ch.qos.logback.core.util.FileUtil
import java.io.File

object KeyGenerator {
    val dictionary = File("src/main/resources/words").readLines()

    fun generateKey(): String {
        val key = mutableListOf<String>()
        for (i in 0..2) {
            key.add(dictionary.random().lowercase())
        }
        println(key.joinToString("-"))
        return key.joinToString("-")
    }
}