package com.kokoo.coroutine

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoroutineApplication

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) {
	runApplication<CoroutineApplication>(*args)

//	Executor.doGlobalScope()
//	Executor.doRunBlocking()

//	runBlocking {
//		Executor.doCoroutineScope()
//	}

//	Executor.doJoin()
	Executor.doRepeat()

	log.info { "ended.." }
}
