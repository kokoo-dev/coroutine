package com.kokoo.coroutine

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoroutineApplication

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) {
	runApplication<CoroutineApplication>(*args)

	BasicExecutor.execute()
	CancellationExecutor.execute()
	ContextDispatcherExecutor.execute()

	log.info { "ended.." }
}
