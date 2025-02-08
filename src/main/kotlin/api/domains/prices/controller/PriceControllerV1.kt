package org.example.api.prices.controller

import org.example.api.prices.service.PriceServiceV1
import org.springframework.web.bind.annotation.*
import lombok.RequiredArgsConstructor
import org.example.api.annotations.OrderCustomAnnotation
import org.example.api.annotations.PageCustomAnnotation
import org.example.api.annotations.SizeCustomAnnotation
import org.example.api.prices.model.Chart
import org.example.api.protocol.Response

@RestController
@RequestMapping("/api/v1/price")
@RequiredArgsConstructor
class PriceControllerV1(
    private val priceService: PriceServiceV1
) {

    @GetMapping("/chart/{excd}/{symbol")
    fun chart(
        @PathVariable symbol: String,
        @PathVariable excd : String,
        @PageCustomAnnotation page: Int,
        @SizeCustomAnnotation size : Int,
        @OrderCustomAnnotation order: String
    ) : Response<Chart> {
        return priceService.chartFromSymbol(symbol, excd, order, page, size)
    }

    @GetMapping("/all-volume")
    fun volume() {

    }
}