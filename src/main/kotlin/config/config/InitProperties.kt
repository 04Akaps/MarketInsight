package org.example.config.config

import org.example.model.data.KeyInfo
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "init")
class InitProperties {
    var keyInfo: KeyInfo = KeyInfo()
}