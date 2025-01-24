package org.example.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CustomException(
    private val codeInterface: CodeInterface,
    additionalMessage: Any? = null
) : RuntimeException(
    "${codeInterface.message}${additionalMessage?.let { " : $it" } ?: ""}"
) {
    // 메시지가 존재하면 let 을 통해서 값을 할당

    init {
        logger.error("Exception created with code: ${codeInterface.code} and message: ${super.message}")
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CustomException::class.java)
    }
}