package org.example.utils

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpMethod
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.model.api.*
import org.example.model.data.KeyInfo
import org.example.model.enums.APIRouter
import org.example.model.mapper.TokenIssuerMapper
import org.example.model.memory.AtomicTokenIssue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class HttpMethod(
    private val info: KeyInfo,
    private val httpClient: HttpClient,
    private val trID : String
) {

    suspend fun getTokenIssue() : TokenIssueResponse? {

        val response : HttpResponse = httpClient.post(APIRouter.TokenIssue.url) {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(
                TokenIssueRequest(GrantType = "client_credentials", AppKey = info.key, AppSecret = info.secret)
            ))
        }

        val responseBody: String = response.readBytes().toString(Charsets.UTF_8)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val temp: TempTokenIssueResponse = Json.decodeFromString<TempTokenIssueResponse>(responseBody)
                TokenIssuerMapper.map(temp)
            }
            else -> {
                val res: TokenIssueResponseError = Json.decodeFromString<TokenIssueResponseError>(responseBody)
                logger.error("Failed to get token issue, code: ${res.code} msg: ${res.description}")
                null
            }
        }
    }

    // https://apiportal.koreainvestment.com/apiservice/apiservice-oversea-stock-quotations#L_0e9fb2ba-bbac-4735-925a-a35e08c9a790
    // -> 해외주식 기간별시세[v1_해외주식-010] 참고
    suspend fun getOverSeasPrice(
        accessToken : String,
        exec : String,
        symbol : String,
        bymd: String,
    ) : OverSeasPriceResponse? {
        val baseUrl : String = APIRouter.OverseasPrice.url


        val queryMap = mapOf(
            "EXCD" to exec,
            "SYMB" to symbol,
            "BYMD" to bymd, // 조회 기준 일자 -> YYYYMMDD
            "GUBN" to 0, // day
            "MODP" to 1 // 수정 주가 반영 여부
        )

        val response : HttpResponse = httpClient.get(baseUrl) {
            contentType(ContentType.Application.Json)

            queryMap.forEach { (key, value) ->
                parameter(key, value)
            }

            headers{
                append("appKey", info.key)
                append("appsecret", info.secret)
                append("tr_id", trID)
                append("Authorization", "Bearer ${accessToken}")
            }
        }

        val responseBody: String = response.readBytes().toString(Charsets.UTF_8)

       return when (response.status) {
            HttpStatusCode.OK -> {
                return Json.decodeFromString<OverSeasPriceResponse>(responseBody)
            }
            else -> {
                val res: OverSeasPriceResponseError = Json.decodeFromString<OverSeasPriceResponseError>(responseBody)
                logger.error("Failed to get token issue, code: ${res.rtCD} msg: ${res.msg1}")
                null
            }
        }

    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(HttpMethod::class.java)
    }
}