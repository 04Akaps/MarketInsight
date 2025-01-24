package org.example.model.api

import com.fasterxml.jackson.annotation.JsonCreator
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import lombok.Getter
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field;


@Serializable
data class APIResponseError(
    @SerialName("error_description")
    val description: String,
    @SerialName("error_code")
    val code: String
)

@Serializable
@Document(collection = "key_history")
data class TokenIssueResponse(
    @Field("accessToken")
    val accessToken: String,
    @Field("accessTokenExpired")
    val accessTokenTokenExpired: Long,
    @Field("tokenType")
    val tokenType: String,
    @Field("expiresIn")
    val expiresIn: Int
)

@Serializable
data class TempTokenIssueResponse(
    @SerialName("access_token")
    val accessToken: String,

    @SerialName("access_token_token_expired")
    val accessTokenTokenExpired: String,

    @SerialName("token_type")
    val tokenType: String,

    @SerialName("expires_in")
    val expiresIn: Int
)