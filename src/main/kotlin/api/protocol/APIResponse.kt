package org.example.api.protocol

import io.ktor.http.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter


@Getter
@Setter
@AllArgsConstructor
data class Response<T>(
    val code: HttpStatusCode = HttpStatusCode.OK,
    val message: String = "",
    val result: T? = null
) {
    companion object {
        fun <T> success(result: T): Response<T> {
            return Response(HttpStatusCode.OK, "Success", result)
        }

        fun <T> error(message: String): Response<T> {
            return Response(HttpStatusCode.InternalServerError , message, null)
        }
    }
}