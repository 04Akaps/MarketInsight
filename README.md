# MarketInsight
한국 투자 증권 API를 활용한 주식 시세 조회 Project [ Kotlin, Spring ]


# 사용하는 API 정리 문서

<h3> key 발급 </h3>

```
URL : https://openapi.koreainvestment.com:9443/oauth2/tokenP
Method : POST

Body

{
    "grant_type" : "client_credentials",
    "appkey" : "",
    "appsecret" : ""
}

Response Example

{
    "access_token": "",
    "access_token_token_expired": "2025-01-25 10:48:30",
    "token_type": "Bearer",
    "expires_in": 86400
}
```