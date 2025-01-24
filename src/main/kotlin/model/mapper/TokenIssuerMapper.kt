package org.example.model.mapper

import org.example.model.api.TempTokenIssueResponse
import org.example.model.api.TokenIssueResponse
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object TokenIssuerMapper {

    fun toTokenIssuer(input: TempTokenIssueResponse): TokenIssueResponse {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val zonedDateTime = ZonedDateTime.parse(input.accessTokenTokenExpired, formatter.withZone(ZoneId.systemDefault()))
        val unixTime = zonedDateTime.toEpochSecond()

        return TokenIssueResponse(
            accessToken = input.accessToken,
            accessTokenTokenExpired = unixTime,
            tokenType = input.tokenType,
            expiresIn = input.expiresIn
        )
    }
}