package org.example.api.custom.web

import io.ktor.http.*
import org.example.api.protocol.Response
import org.example.exception.CodeInterface
import org.example.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException


@ControllerAdvice
class GlobalException {

    @ExceptionHandler(MethodArgumentTypeMismatchException::class) // 타입에러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<Response<Any>> {
        val errorMessage = "Invalid value for parameter '" + ex.name

        val errorResponse: Response<Any> = BadRequest(errorMessage)

        return ResponseEntity<Response<Any>>(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingParams(ex: MissingServletRequestParameterException): ResponseEntity<Response<Any>> {
        val errorMessage = "Required parameter '" + ex.parameterName + "' is missing"


        val errorResponse: Response<Any> = BadRequest(errorMessage)

        return ResponseEntity<Response<Any>>(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: CustomException): ResponseEntity<Response<Any>> {
        val codeInterface: CodeInterface = ex.getCodeInterface()
        val errorMessage = codeInterface.message

        val errorCode = codeInterface.code

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Response.error(errorCode, errorMessage))
    }

    private fun BadRequest(msg: String): Response<Any> {
        return Response(
            HttpStatusCode.BadRequest.value,
            "Bad Request",
            msg
        )
    }
}