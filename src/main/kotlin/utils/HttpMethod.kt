package org.example.utils

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.exception.CustomException
import org.example.model.api.APIResponseError
import org.example.model.api.TempTokenIssueResponse
import org.example.model.api.TokenIssueRequest
import org.example.model.api.TokenIssueResponse
import org.example.model.data.KeyInfo
import org.example.model.enums.APIRouter
import org.example.model.mapper.TokenIssuerMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class HttpMethod(
    private val info: KeyInfo,
    private val httpClient: HttpClient
) {

    suspend fun getTokenIssue() : TokenIssueResponse? {

        val response : HttpResponse = httpClient.post(APIRouter.TokenIssue.url) {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(
                TokenIssueRequest(GrantType = "client_credentials", AppKey = info.key, AppSecret = info.secret)
            ))
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody: String = response.readBytes().toString(Charsets.UTF_8)
                val temp: TempTokenIssueResponse = Json.decodeFromString<TempTokenIssueResponse>(responseBody)
                TokenIssuerMapper.toTokenIssuer(temp)
            }
            else -> {
                val responseBody: String = response.readBytes().toString(Charsets.UTF_8)
                val res: APIResponseError = Json.decodeFromString<APIResponseError>(responseBody)
                logger.error("Failed to get token issue, code: ${res.code} msg: ${res.description}")
                null
            }
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CustomException::class.java)
    }
}