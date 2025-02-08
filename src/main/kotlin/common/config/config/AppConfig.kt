package org.example.config.config

import org.example.model.data.KeyInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class AppConfig(private val initProperties: InitProperties) {

    @Value("\${init.keyInfo.trID}")
    private val trID: String = ""

    @Bean
    fun keyInfo(): KeyInfo {
        return initProperties.keyInfo
    }

    @Bean
    fun trID(): String {
        return trID
    }

    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.threadNamePrefix = "task-scheduler-" // 쓰레드 이름
        scheduler.initialize() // 초기화
        return scheduler
    }
}