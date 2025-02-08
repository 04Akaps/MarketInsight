package org.example.api.prices.model

import kotlinx.serialization.Serializable
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

data class Chart(
    val symbol : String,
    val data : List<PriceInfo>,
)

data class PriceInfo(
    val price : String,
    val date : String
)