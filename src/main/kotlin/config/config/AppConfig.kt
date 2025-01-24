package org.example.config.config

import org.example.model.data.KeyInfo
import org.example.model.data.Resource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class AppConfig(private val initProperties: InitProperties) {

    @Value("\${init.duration}")
    private var duration: Int = 0

    @Bean
    fun keyInfo(): KeyInfo {
        return initProperties.keyInfo
    }

    @Bean
    fun resources(): List<Resource> {
        return initProperties.resources
    }

    @Bean
    fun taskScheduler(): TaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = initProperties.resources.size // pool 크기
        scheduler.threadNamePrefix = "task-scheduler-" // 쓰레드 이름
        scheduler.initialize() // 초기화
        return scheduler
    }

    @Bean
    fun duration(): Int {
        return duration
    }

}