package org.example.api.prices.controller

import org.example.api.prices.service.PriceServiceV1
import org.springframework.web.bind.annotation.*
import lombok.RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/price")
@RequiredArgsConstructor
class PriceControllerV1(
    private val priceService: PriceServiceV1
) {

    @GetMapping("/chart")
    fun chart() {

    }

    @GetMapping("/all-volume")
    fun volume() {

    }
}