package org.example.model.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field;


@Serializable
data class TokenIssueResponseError(
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


@Serializable
data class OverSeasPriceResponse(
    @SerialName("output1")
    val output1: Output1,

    @SerialName("output2")
    val output2: List<Output2>,

    @SerialName("rt_cd")
    val rtCd: String,

    @SerialName("msg_cd")
    val msgCd: String,

    @SerialName("msg1")
    val msg1: String
)

@Serializable
data class Output1(
    @SerialName("rsym")
    val rsym: String,

    @SerialName("zdiv")
    val zdiv: String,

    @SerialName("nrec")
    val nrec: String
)

@Serializable
data class Output2(
    @SerialName("xymd")
    val xymd: String, // 조회 데이터 날짜 : 20250124

    @SerialName("clos")
    val clos: String, // 해당 일자 종가 : 406.5800


    /*
        1 : 상한
        2 : 상승
        3 : 보합
        4 : 하한
        5 : 하락
     */
    @SerialName("sign")
    val sign: String, // 대비 기호

    @SerialName("diff")
    val diff: String, // 전일 종가와 해당 날짜의 종가 차이

    @SerialName("rate")
    val rate: String, // 해당 전일 대비 / 해당일 종가 * 100

    @SerialName("open")
    val open: String, // 해당일 최초 거래가격

    @SerialName("high")
    val high: String, // 해당일 가장 높은 거래가격

    @SerialName("low")
    val low: String, // 해당일 가장 낮은 거래가격

    @SerialName("tvol")
    val tvol: String, // 해당일 거래량

    @SerialName("tamt")
    val tamt: String, // 해당일 거래대금

    @SerialName("pbid")
    val pbid: String, // 마지막 체결이 발생한 시점의 매수호가

    @SerialName("vbid")
    val vbid: String, // 매수호가잔량

    @SerialName("pask")
    val pask: String, // 마지막 체결이 발생한 시점의 매도호가

    @SerialName("vask")
    val vask: String // 매도호가잔량
)

@Serializable
data class OverSeasPriceResponseError(
    @SerialName("rt_cd")
    val rtCD: String,

    @SerialName("msg_cd")
    val msgCd: String,

    @SerialName("msg1")
    val msg1: String
)

@Serializable
@Document(collection = "resources")
data class RoutineResources(
    @Field("symbol")
    val symbol: String,

    @Field("excd")
    val excd: List<String>,

    @Field("updatedDay")
    val updatedDay: Int,
)


@Serializable
@Document(collection = "price_history")
data class PriceHistoryDoc(
    @Field("symbol")
    val symbol : String,

    @Field("price")
    val price : String,

    @Field("date")
    val date : String,

    @Field("excd")
    val excd : String,
)