package org.example.model.data

data class KeyInfo(
    val key: String = "",
    val secret: String = ""
)

data class Resource(
    val symbol: String = "",
    val gubn : Long = 0L
)