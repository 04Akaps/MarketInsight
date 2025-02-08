package org.example.api.domains.resources.model

import kotlinx.serialization.Serializable
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Serializable
@Document(collection = "resources")
data class Resources(
    @Field("symbol")
    val symbol: String,

    @Field("excd")
    val excd : List<String>
)