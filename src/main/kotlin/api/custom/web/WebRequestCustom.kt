package org.example.api.custom.web

import org.example.api.annotations.*
import org.example.exception.CustomException
import org.example.exception.ErrorCode
import org.springframework.core.MethodParameter
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*

@Component
class WebRequestCustom(
    private val ALLOWED_VALUES: List<String> = mutableListOf("ASC", "DESC", "asc", "desc")
) : HandlerMethodArgumentResolver{
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        // Request에 대한 Annotaion 추가 되면, || 조건을 통해서 검증하는 로직을 추가 한다.
        return parameter.hasParameterAnnotation(OrderCustomAnnotation::class.java)
                || parameter.hasParameterAnnotation(PageCustomAnnotation::class.java)
                || parameter.hasParameterAnnotation(SizeCustomAnnotation::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        @Nullable mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        @Nullable binderFactory: WebDataBinderFactory?
    ): Any? {
        if (parameter.hasParameterAnnotation(OrderCustomAnnotation::class.java)) {
            val customAnnotation: OrderCustomAnnotation? = parameter.getParameterAnnotation(OrderCustomAnnotation::class.java)

            webRequest.getParameter(Objects.requireNonNull(parameter.parameterName))?.let {
                if (!ALLOWED_VALUES.contains(it)) {
                    throw CustomException(ErrorCode.NotSupportedOrderRequest, it)
                }

                return it.lowercase()
            } ?: run {
                if (customAnnotation != null) {
                    return customAnnotation.defaultSort.lowercase()
                }else {
                    return "DESC".lowercase()
                }
            }
        }

        if (parameter.hasParameterAnnotation(PageCustomAnnotation::class.java)) {
            val customAnnotation: PageCustomAnnotation? = parameter.getParameterAnnotation(PageCustomAnnotation::class.java)

            webRequest.getParameter((Objects.requireNonNull(parameter.parameterName)))?.let {
                return it
            } ?: run {
                if (customAnnotation != null) {
                    return customAnnotation.defaultPage
                } else {
                    return 0
                }
            }

        }

        if (parameter.hasParameterAnnotation(SizeCustomAnnotation::class.java)) {
            val customAnnotation: SizeCustomAnnotation? = parameter.getParameterAnnotation(SizeCustomAnnotation::class.java)

            webRequest.getParameter((Objects.requireNonNull(parameter.parameterName)))?.let {
                return it
            } ?: run {
                if (customAnnotation != null) {
                    return customAnnotation.defaultSize
                } else {
                    return 0
                }
            }
        }

        return null
    }

}