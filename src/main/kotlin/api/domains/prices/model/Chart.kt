package org.example.api.prices.model

data class Chart(
    val symbol : String,
    val data : List<PriceInfo>,
)

data class PriceInfo(
    val price : String,
    val date : String
)