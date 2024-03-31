package com.kokoo.coroutine

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*

class CancellationExecutor {
    companion object {
        private val log = KotlinLogging.logger {}

        fun execute() {
            cancelAndJoin()
            tryCatch()
            tryFinally()
            tryFinallyWithNonCancellable()
            isActive()
        }

        private fun cancelAndJoin() = runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        log.info { "job: I'm sleeping ${i++} ..." }
                        nextPrintTime += 500
                    }
                }
            }
            delay(1300)
            log.info { "main: I'm tired of waiting!" }
            job.cancelAndJoin()
            log.info { "main: Now I can quit." }

            // print:
            // job: I'm sleeping 0 ...
            // job: I'm sleeping 1 ...
            // job: I'm sleeping 2 ...
            // main: I'm tired of waiting!
            // job: I'm sleeping 3 ...
            // job: I'm sleeping 4 ...
            // main: Now I can quit.
        }

        private fun tryCatch() = runBlocking {
            val job = launch(Dispatchers.Default) {
                repeat(5) { i ->
                    try {
                        log.info { "job: I'm sleeping $i ..." }
                        delay(500)
                    } catch (e: Exception) {
                        log.error { e }
                    }
                }
            }
            delay(1300)
            log.info { "main: I'm tired of waiting!" }
            job.cancelAndJoin()
            log.info { "main: Now I can quit." }
        }

        private fun tryFinally() = runBlocking {
            val job = launch(Dispatchers.Default) {
                try {
                    repeat(5) { i ->
                        log.info { "job: I'm sleeping $i ..." }
                        delay(500)
                    }
                } finally {
                    log.info { "job: I'm running finally" }
                }
            }
            delay(1300)
            log.info { "main: I'm tired of waiting!" }
            job.cancelAndJoin()
            log.info { "main: Now I can quit." }

            // print:
            // job: I'm sleeping 0 ...
            // job: I'm sleeping 1 ...
            // job: I'm sleeping 2 ...
            // main: I'm tired of waiting!
            // job: I'm running finally
            // main: Now I can quit.
        }

        private fun tryFinallyWithNonCancellable() = runBlocking {
            val job = launch(Dispatchers.Default) {
                try {
                    repeat(5) { i ->
                        log.info { "job: I'm sleeping $i ..." }
                        delay(500)
                    }
                } finally {
                    withContext(NonCancellable) {
                        log.info { "job: I'm running finally" }
                        delay(1000)
                        log.info { "job: 동기적 처리" }
                    }
                }
            }
            delay(1300)
            log.info { "main: I'm tired of waiting!" }
            job.cancelAndJoin()
            log.info { "main: Now I can quit." }

            // print:
            // job: I'm sleeping 0 ...
            // job: I'm sleeping 1 ...
            // job: I'm sleeping 2 ...
            // main: I'm tired of waiting!
            // job: I'm running finally
            // job: 동기적 처리
            // main: Now I can quit.
        }

        private fun isActive() = runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                var i = 0
                while (isActive) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        log.info { "job: I'm sleeping ${i++} ..." }
                        nextPrintTime += 500
                    }
                }
            }
            delay(1300)
            log.info { "main: I'm tired of waiting!" }
            job.cancelAndJoin()
            log.info { "main: Now I can quit." }

            // print:
            // job: I'm sleeping 0 ...
            // job: I'm sleeping 1 ...
            // job: I'm sleeping 2 ...
            // main: I'm tired of waiting!
            // main: Now I can quit.
        }
    }
}