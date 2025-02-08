package org.example.TaskManager

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.*
import lombok.RequiredArgsConstructor
import org.example.exception.CustomException
import org.example.exception.ErrorCode
import org.example.model.api.RoutineResources
import org.example.model.memory.AtomicTokenIssue
import org.example.utils.MongoMethod
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Service
import java.lang.Runnable
import java.time.*
import kotlinx.coroutines.launch
import org.example.TaskManager.marketHandler.MarketHandler
import kotlin.system.exitProcess


@Service
@RequiredArgsConstructor
class TaskManager (
    private val taskScheduler: ThreadPoolTaskScheduler,
    private val atomicTokenIssue: AtomicTokenIssue,
    private val mongoMethod: MongoMethod,
    private val marketHandler: MarketHandler
) {

    private val exitExceptionHandler = CoroutineExceptionHandler { _, exception ->
        logger.error("Exception occurred while loading token issues", exception)
        exitProcess(1)
    }

    @PostConstruct
    private fun resolveInitTasks() = runBlocking {
        var resources : List<RoutineResources> = emptyList()

        try {
            atomicTokenIssue.resolveValue()
            resources = mongoMethod.findAllResources()
            taskScheduler.poolSize = resources.size
        } catch (e: Exception) {
            throw CustomException(ErrorCode.ExceptionError, e.toString())
        } finally {
            logger.info("Successfully loaded token issues")
            startScheduledTasks(resources)
        }
    }

    private suspend fun startScheduledTasks(resources : List<RoutineResources>) {
        CoroutineScope(Dispatchers.Default + exitExceptionHandler).launch {
            val jobs = resources.map { resource -> launch { marketHandler.dailyTask(resource) } }
            jobs.forEach { it.join() }
        }

        val now = ZonedDateTime.now(ZoneId.systemDefault()) // 현재 시간
        val nextMidnight = now.plusDays(1).toLocalDate().atStartOfDay(now.zone) // 다음 자정 시간
        val initialDelay = Duration.between(now, nextMidnight) // 시간 차이

        val startTime = now.toInstant().plus(initialDelay) // 다음 시작 시간
        val period = Duration.ofDays(1) // 다시 동작하는 주기

        val dailyTask = Runnable {
            CoroutineScope(Dispatchers.Default + exitExceptionHandler).launch {
                resources.forEach { resource -> launch { marketHandler.dailyTask(resource) } }
            }
        }

        // 자정마다 한번씩 실행
        taskScheduler.scheduleAtFixedRate(dailyTask, startTime, period);
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TaskManager::class.java)
    }

}

