package org.example.model.api

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TokenIssueRequest(
    @SerialName("grant_type")  @field:NotNull @field:NotEmpty val
    GrantType: String,

    @SerialName("appkey") @field:NotNull @field:NotEmpty
    val AppKey: String,

    @SerialName("appsecret") @field:NotNull @field:NotEmpty
    val AppSecret: String
)