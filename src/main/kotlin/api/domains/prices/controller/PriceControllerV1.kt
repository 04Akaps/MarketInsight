package org.example.api.prices.controller

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.example.api.prices.service.PriceServiceV1
import org.springframework.web.bind.annotation.*
import lombok.RequiredArgsConstructor
import org.example.api.annotations.OrderCustomAnnotation
import org.example.api.annotations.PageCustomAnnotation
import org.example.api.annotations.SizeCustomAnnotation
import org.example.api.domains.prices.model.AllVolume
import org.example.api.prices.model.Chart
import org.example.api.protocol.Response
import org.example.exception.CustomException
import org.example.exception.ErrorCode

@RestController
@RequestMapping("/api/v1/price")
@RequiredArgsConstructor
class PriceControllerV1(
    private val priceService: PriceServiceV1
) {

    @GetMapping("/chart/{excd}/{symbol}")
    fun chart(
        @PathVariable symbol: String,
        @PathVariable excd : String,
        @PageCustomAnnotation page: Int,
        @SizeCustomAnnotation size : Int,
        @OrderCustomAnnotation order: String
    ) : Response<Chart> {
        return priceService.chartFromSymbol(symbol, excd, order, page, size)
    }

    @GetMapping("/all-volume/{excd}/{symbol}")
    fun volume(
        @PathVariable symbol: String,
        @PathVariable excd : String,
        @RequestParam @NotNull @NotEmpty startDate : String,
        @RequestParam @NotNull @NotEmpty endDate : String,
    ) : Response<AllVolume> {
        val requestCheck : Boolean =  startDate.toInt() < endDate.toInt()

        return when (requestCheck) {
            true -> throw CustomException(ErrorCode.StartDateLowerThanEndDate)
            else -> priceService.allVolume(symbol, excd, startDate, endDate)
        }
    }
}