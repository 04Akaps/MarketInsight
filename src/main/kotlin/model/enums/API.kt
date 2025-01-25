package org.example.model.enums

enum class APIRouter(val url: String) {
    TokenIssue("https://openapi.koreainvestment.com:9443/oauth2/tokenP"),
    OverseasPrice("https://openapi.koreainvestment.com:9443/uapi/overseas-price/v1/quotations/dailyprice");
}