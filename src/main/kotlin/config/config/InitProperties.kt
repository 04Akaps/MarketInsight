package org.example.config.config

import org.example.model.data.KeyInfo
import org.example.model.data.Resource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "init")
class InitProperties {
    var keyInfo: KeyInfo = KeyInfo()
    var resources: List<Resource> = emptyList()
}