# MarketInsight
한국 투자 증권 API를 활용한 주식 시세 조회 Project [ Kotlin, Spring ]

# 한국 투자 증권 API 정리 문서

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

3. 한국 투자증권에서 데이터를 제공하지 않는 경우 Logging만 진행하며, 에러 전파를 하지 않는다.
```
Unable to determine overseas data for symbol TSLA-SZS
```

# 데이터 저장 구조

1. Auth Key
```
{
  "accessToken": "",
  "accessTokenExpired": {
    "$numberLong": "1739074535"
  },
  "expiresIn": 86400,
  "tokenType": "Bearer"
}
```

2. Price History
```
{
  "excd": "NAS",
  "date": "20250206",
  "symbol": "TSLA",
  "price": "374.3200"
}
```


3. Resources
```
{
  "symbol": "TSLA",
  "excd": [
    "HKS",
    "NYS",
    "NAS",
    "AMS",
    "TSE",
    "SHS",
    "SZS",
    "SHI",
    "SZI",
    "HSX",
    "HNX",
    "TestError"
  ]
}
```


# API 작업 내용

1. API의 에러케이스에 대해서는 Exception을 통해서 처리
- `package org.example.exception` 파일 참고

2. Response 객체를 범용적으로 사용하여, Response 포멧을 유지
- `package org.example.api.protocol`

3. CustomRequestAnnotation을 활용
- `package org.example.api.custom.web`

## API Spec

```
1. api/v1/price/chart/{excd}/{symbol}

거래소와, 항목을 Path값을 통해서 수용하고, 추가로 Query값을 적용하요 원하는 가격 데이터를 노출

2. api/v1/price/all-volume/{excd}/{symbol}

특정 항목에 대한 총 가격을 노출
- startDate, endDate를 추가로 받아서 처리

3. api/v1/resource/all-resources

수집중인 총 resources를 노출

3. api/v1/resource/resource/{symbol}

특정 항목에 대한 resources 정보를 노출
```