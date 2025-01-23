package org.example.TaskManager

import kotlinx.coroutines.*
import kotlin.random.Random

class TaskManager(private val scope: CoroutineScope) {

    private val jobs = mutableListOf<Job>()

    fun startTasks(tasks: List<String>) {
        tasks.forEach { task ->
            val job = scope.launch {
                executeTaskPeriodically(task)
            }
            jobs.add(job)
        }


        Runtime.getRuntime().addShutdownHook(Thread {
            println("Shutting down gracefully...")
            scope.cancel()
        })

        Thread.currentThread().join()
    }

    private suspend fun executeTaskPeriodically(task: String) {
        println("Executing $task at ${System.currentTimeMillis()}")
        delay(Random.nextLong(1000L, 3000L)) // 1~3초 간격으로 실행
    }


}