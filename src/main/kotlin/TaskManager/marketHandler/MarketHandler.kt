package org.example.TaskManager.marketHandler

import com.mongodb.bulk.BulkWriteResult
import kotlinx.coroutines.*
import lombok.RequiredArgsConstructor
import org.example.model.api.OverSeasPriceResponse
import org.example.model.api.PriceHistoryDoc
import org.example.model.api.RoutineResources
import org.example.model.memory.AtomicTokenIssue
import org.example.utils.HttpMethod
import org.example.utils.MongoMethod
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
@RequiredArgsConstructor
class MarketHandler (
    private val atomicTokenIssue: AtomicTokenIssue,
    private val mongoMethod: MongoMethod,
    private val httpMethod: HttpMethod
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        logger.error("Exception occurred while loading token issues", exception)
    }

    suspend fun dailyTask(resource : RoutineResources) {
        val symbol: String = resource.symbol

        // 부모 coroutine Scope를 사용 및 예외 처리 공유

        coroutineScope {
            val jobs = resource.excd.map { ec -> launch {
                val latestDoc: PriceHistoryDoc? = mongoMethod.findLatestPriceHistory(symbol, ec)

                latestDoc?.let {
                    val latestDate : String = ""

                } ?: run {
                    // 데이터가 없다면 초반부터 수집
                    val overSeasData : OverSeasPriceResponse? = httpMethod.getOverSeasPrice(
                        atomicTokenIssue.resolveValue().accessToken,
                        ec,
                        symbol,
                        ""
                    )

                    if (overSeasData != null) {
                        launch(Dispatchers.Default + exceptionHandler) {
                            processOverSeasData(symbol, ec, overSeasData)
                        }
                    } else {
                        logger.info("Unable to determine overseas data for symbol ${symbol}-${ec}")
                    }

                }
            }}

            jobs.forEach { it.join() }
        }
    }

    private suspend fun processOverSeasData(symbol: String, excd:String, it: OverSeasPriceResponse) {
        val result: BulkWriteResult = mongoMethod.upsertPriceHistory(symbol, excd, it.output2)
        logger.info("${symbol}-${excd} bulk inserted count ${result.insertedCount}, modified count ${result.modifiedCount}")
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MarketHandler::class.java)
    }
}