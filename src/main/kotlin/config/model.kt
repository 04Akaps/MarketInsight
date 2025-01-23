package org.example.config

data class KeyInfo(
    var key: String = "",
    var secret: String = ""
)

data class Resource(
    var symbol: String = "",
    var market: String = ""
)