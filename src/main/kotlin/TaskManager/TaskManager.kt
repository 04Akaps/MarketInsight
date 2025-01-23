package org.example.TaskManager

import jakarta.annotation.PostConstruct
import org.example.config.KeyInfo
import org.example.config.Resource
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

import java.util.concurrent.ScheduledFuture

@Service
class TaskManager (
    private val keyInfo: KeyInfo,
    private val resources: List<Resource>,
    private val taskScheduler: TaskScheduler,
    private val duration: Int
) {

    @PostConstruct
    fun startTasks() {
        println("KeyInfo: $keyInfo")
        println("Resources: $resources")

        startScheduledTasks()
    }

    private fun startScheduledTasks() {
        for (resource in resources) {
            val task = Runnable {
                performScheduledTask(resource)
            }

            // 현재 시간 이후 duration 시작, 그 이후 duration 마다 주기적으로 실행
            val initialDelay = Instant.now().plus(Duration.ofSeconds(duration.toLong()))
            val interval = Duration.ofSeconds(duration.toLong())

            taskScheduler.scheduleAtFixedRate(task, initialDelay, interval)
        }
    }

    private fun performScheduledTask(resource: Resource) {
        println(resource.market + " " + resource.symbol)
    }

}

