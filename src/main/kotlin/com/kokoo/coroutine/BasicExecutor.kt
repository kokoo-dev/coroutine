package com.kokoo.coroutine

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*

class BasicExecutor {
    companion object {
        private val log = KotlinLogging.logger {}

        fun execute() {
            doGlobalScope()
            doRunBlocking()

            runBlocking {
                doCoroutineScope()
            }

            join()
            repeat()
        }

        @OptIn(DelicateCoroutinesApi::class)
        private fun doGlobalScope() {
            // delicate
            GlobalScope.launch {
                delay(1000)
                log.info { "world" }
            }

            log.info { "hello" }

            // print: hello world
        }

        private fun doRunBlocking() = runBlocking {
            launch {
                delay(1000)
                log.info { "world" }
            }

            log.info { "hello" }

            delay(1500)
            // print: hello world, 완료까지 대기
        }

        private suspend fun doCoroutineScope() = coroutineScope {
            launch {
                delay(2000)
                log.info { "world2" }
            }
            launch {
                delay(1000)
                log.info { "world1" }
            }

            log.info { "hello" }

            delay(1500)
            // print: hello world1 world2
        }

        private fun join() = runBlocking {
            val job = launch {
                delay(1000)
                log.info { "world" }
            }

            log.info { "hello" }
            job.join() // 하위 코루틴 완료까지 대기

            log.info { "done" }
            // print: hello world done
        }

        private fun repeat() = runBlocking {
            repeat(50_000) { // launch a lot of coroutines
                launch {
                    delay(5000L)
                    print(".")
                }
            }
            // coroutine은 가벼워서 OOM이 발생하지 않음
        }
    }
}