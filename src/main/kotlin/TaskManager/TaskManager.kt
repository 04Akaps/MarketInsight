package org.example.TaskManager

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.runBlocking
import lombok.RequiredArgsConstructor
import org.example.exception.CustomException
import org.example.model.api.TokenIssueResponse
import org.example.model.data.KeyInfo
import org.example.model.data.Resource
import org.example.utils.HttpMethod
import org.example.utils.MongoMethod
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
@RequiredArgsConstructor
class TaskManager (
    private val resources: List<Resource>,
    private val taskScheduler: TaskScheduler,
    private val duration: Int,
    private val httpMethod: HttpMethod,
    private val mongoMethod: MongoMethod
) {

    @PostConstruct
    private  fun resolveInitTasks() {
        runBlocking {
            try {

                val key: TokenIssueResponse? = mongoMethod.findKeyHistory()

                key?.let {
                    println("keykeykey $key")
                }

                println("serser")
            } catch (e: Exception) {
                println("Error in resolveInitTasks: ${e.message}")
                e.printStackTrace()
            }
        }
    }


//    println(mongoMethod.findKeyHistory())
//            println(httpMethod.getTokenIssue())

    private fun startScheduledTasks() {
        val initialDelay = Instant.now().plus(Duration.ofSeconds(duration.toLong()))
        val interval = Duration.ofSeconds(duration.toLong())

        for (resource in resources) {
            val task = Runnable {
                performScheduledTask(resource)
            }

            // 현재 시간 이후 duration 시작, 그 이후 duration 마다 주기적으로 실행

            taskScheduler.scheduleAtFixedRate(task, initialDelay, interval)
        }
    }

    private fun performScheduledTask(resource: Resource) {
        println(resource.market + " " + resource.symbol)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CustomException::class.java)
    }

}

