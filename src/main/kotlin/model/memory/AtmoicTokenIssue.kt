package org.example.model.memory

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import lombok.RequiredArgsConstructor
import org.example.exception.CustomException
import org.example.exception.ErrorCode
import org.example.model.api.TokenIssueResponse
import org.example.utils.HttpMethod
import org.example.utils.MongoMethod
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference


@Component
@RequiredArgsConstructor
class AtomicTokenIssue(
    private val mongoMethod: MongoMethod,
    private val httpMethod: HttpMethod
) {
    private val value = AtomicReference<TokenIssueResponse>(null)
    private val mutex = Mutex()

    suspend fun getValue(): TokenIssueResponse {
        return mutex.withLock {
            value.get()?.let {
                // 있다면, 시간을 비교하여 기존 값을 사용하고
                val accessTokenExpiredUnix = it.accessTokenTokenExpired
                val currentUnixTime = Instant.now().epochSecond

                if (accessTokenExpiredUnix > currentUnixTime) {
                    return@withLock it
                } else {
                    // 시간이 초과가 되었다면, TokenIssue 새로 발급한다.
                    return@withLock getTokenIssue(false)
                }
            } ?: run {
                // 없다면, 새로 발급한다.
                return@withLock getTokenIssue(true)
            }
        }
    }

    fun setValue(v: TokenIssueResponse) {
        value.set(v)
    }


    private fun getTokenIssue(init: Boolean): TokenIssueResponse {
        var res: TokenIssueResponse? = null

        runBlocking {
            if (init) {
                logger.info("token issue init")

                mongoMethod.findKeyHistory()?.let { response ->
                    val accessTokenExpiredUnix = response.accessTokenTokenExpired
                    val currentUnixTime = Instant.now().epochSecond

                    if (accessTokenExpiredUnix > currentUnixTime) {
                        logger.info("token issue using db data when time remain")
                        // 아직 유효 시간이 남아 있다면, 해당 token을 사용하여 처리
                        res = response
                    } else {
                        logger.info("token issue refresh when init")
                        val httpRes : TokenIssueResponse? = httpMethod.getTokenIssue()

                        httpRes?.let {
                            val updated :Boolean = mongoMethod.updateKeyHistory(it)

                            if (!updated) {
                                throw CustomException(ErrorCode.FailedToSetKeyHistory)
                            } else {
                                logger.info("token issue store when init")
                                res = it
                            }
                        } ?: {
                            throw CustomException(ErrorCode.FailedToCallClient, "getTokenIssue")
                        }

                    }
                } ?: run {
                    throw CustomException(ErrorCode.FailedToGetKeyHistoryFromMongo)
                }
            } else {
                logger.info("token issue expired")
                // 시간 초과가 되어서 처리해야 하는 함수
                val httpRes : TokenIssueResponse? = httpMethod.getTokenIssue()

                httpRes?.let {
                    val updated :Boolean = mongoMethod.updateKeyHistory(it)

                    if (!updated) {
                        throw CustomException(ErrorCode.FailedToSetKeyHistory)
                    } else {
                        logger.info("token issue store when timeout")
                        res = it
                    }
                } ?: {
                    throw CustomException(ErrorCode.FailedToCallClient, "getTokenIssue")
                }
            }
        }

        return res ?: throw CustomException(ErrorCode.FailedToCallClient, "getTokenIssue")
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AtomicTokenIssue::class.java)
    }
}