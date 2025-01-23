package org.example.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "init")
class InitProperties {
    var keyInfo: KeyInfo = KeyInfo()
    var resources: List<Resource> = emptyList()
}