package org.example.api.protocol

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter


@Getter
@Setter
@AllArgsConstructor
data class Response<T>(
    var code: Int = 0,
    var message: String = "",
    var result: T? = null
) {
    companion object {
        fun <T> success(result: T): Response<T> {
            return Response(200, "Success", result)
        }

        fun <T> error(errCode: Int,message: String): Response<T> {
            return Response(errCode , message, null)
        }
    }
}