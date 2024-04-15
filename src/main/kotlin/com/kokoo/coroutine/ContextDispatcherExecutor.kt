package com.kokoo.coroutine

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*

class ContextDispatcherExecutor {

    companion object {
        private val log = KotlinLogging.logger {}

        fun execute() {
            doDispatcherThreadName()
            doChildrenCoroutine()
            doParentCoroutine()
        }

        private fun doDispatcherThreadName() = runBlocking {
            launch {
                log.info { "main runBlocking: I'm working in thread ${Thread.currentThread().name}" }
            }
            launch(Dispatchers.Unconfined) {
                log.info { "Unconfined: I'm working in thread ${Thread.currentThread().name}" }
            }
            launch(Dispatchers.Default) {
                log.info { "Default: I'm working in thread ${Thread.currentThread().name}" }
            }
            launch(Dispatchers.IO) {
                log.info { "IO: I'm working in thread ${Thread.currentThread().name}" }
            }
            // print
            // Unconfined: I'm working in thread main
            // Default: I'm working in thread DefaultDispatcher-worker-2
            // main runBlocking: I'm working in thread main
            // IO: I'm working in thread DefaultDispatcher-worker-2
        }

        private fun doChildrenCoroutine() = runBlocking {
            val request = launch {
                launch(Job()) {
                    log.info { "job1: 내 자신의 Job에서 독립적으로 실행" }
                    delay(1000)
                    log.info { "job1: request 취소에 영향을 받지 않음" }
                }
                launch {
                    delay(100)
                    log.info { "job2: request 코루틴의 자식" }
                    delay(1000)
                    log.info { "job2: 상위 요청 취소 시 영향을 받음" }
                }
            }
            delay(500)
            request.cancel()
            delay(1000)
            // print
            // job1: 내 자신의 Job에서 독립적으로 실행
            // job2: request 코루틴의 자식
            // job1: request 취소에 영향을 받지 않음
        }

        private fun doParentCoroutine() = runBlocking {
            val request = launch {
                repeat(3) { i -> // launch a few children jobs
                    launch  {
                        delay((i + 1) * 200L) // variable delay 200ms, 400ms, 600ms
                        log.info { "Coroutine $i: done" }
                    }
                }
                log.info { "request: done" }
            }
            request.join()
            log.info { "request is complete" }
            // print
            // request: done
            // Coroutine 0: done
            // Coroutine 1: done
            // Coroutine 2: done
            // request is complete
        }
    }
}