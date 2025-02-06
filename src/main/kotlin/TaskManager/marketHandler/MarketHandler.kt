package org.example.TaskManager.marketHandler

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        val scope = CoroutineScope(Dispatchers.IO + exceptionHandler)

        for (ec in resource.excd) {
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

                overSeasData?.let {
                    val job = scope.launch(Dispatchers.Default) {
                        processOverSeasData(symbol, ec, it)
                    }
                    job.join()
                } ?: {
                    logger.info("Unable to determine over seas data for symbol $symbol")
                }

            }

        }
    }

    private suspend fun processOverSeasData(symbol: String, excd:String, it: OverSeasPriceResponse) {
        mongoMethod.upsertPriceHistory(symbol, excd, it.output2)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MarketHandler::class.java)
    }
}