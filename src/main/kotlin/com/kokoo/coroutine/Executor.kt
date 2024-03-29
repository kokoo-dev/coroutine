package com.kokoo.coroutine

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*

class Executor {
    companion object {
        private val log = KotlinLogging.logger {}

        @OptIn(DelicateCoroutinesApi::class)
        fun doGlobalScope() {
            // delicate
            GlobalScope.launch {
                delay(1000)
                log.info { "world" }
            }

            log.info { "hello" }

            // print: hello world
        }

        fun doRunBlocking() = runBlocking {
            launch {
                delay(1000)
                log.info { "world" }
            }

            log.info { "hello" }

            delay(1500)
            // print: hello world, 완료까지 대기
        }

        suspend fun doCoroutineScope() = coroutineScope {
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

        fun doJoin() = runBlocking {
            val job = launch {
                delay(1000)
                log.info { "world" }
            }

            log.info { "hello" }
            job.join() // 하위 코루틴 완료까지 대기

            log.info { "done" }
            // print: hello world done
        }

        fun doRepeat() = runBlocking {
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