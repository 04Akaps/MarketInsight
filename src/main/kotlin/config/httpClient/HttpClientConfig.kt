package org.example.config.httpClient

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.util.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration



@Configuration
class HttpClientConfig {

    @Bean
    fun httpClient() : HttpClient {
        return HttpClient(CIO) {
            engine {
                requestTimeout = 30_000 // 30초 (밀리초 단위)
            }

            install(DefaultRequest) {
                headers.appendIfNameAbsent(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
        }
    }
}