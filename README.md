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

<h3> 해외 시세 조회 </h3>

```
URL : https://openapi.koreainvestment.com:9443/uapi/overseas-price/v1/quotations/dailyprice
Method : GET

Query Parameter

AUTH : ""
EXCD : String
SYMB : String
GUBN : String
BYMD : ""
MODP : Int

Header

authorization
appkey
appsecret
```
- 자세한 정보는 https://apiportal.koreainvestment.com/apiservice/apiservice-oversea-stock-quotations#L_0e9fb2ba-bbac-4735-925a-a35e08c9a790 참고


# 데이터 저장 기준

1. SYMB(심볼), EXCD(마켓)을 기준으로 저장으 한다.
```
TSLA를 만약 조회한다고 한다면,
TSLA를 Key로 잡고 마켓만큼 코루틴 생성 및 관리
```

2. 하루 기준으로 저장을 한다.
```
GUBN = 0
```
