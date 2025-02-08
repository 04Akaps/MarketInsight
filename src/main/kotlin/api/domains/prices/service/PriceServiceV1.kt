package org.example.api.prices.service

import kotlinx.coroutines.coroutineScope
import lombok.RequiredArgsConstructor
import org.example.api.prices.model.Chart
import org.example.api.prices.model.PriceInfo
import org.example.api.protocol.Response
import org.example.exception.CustomException
import org.example.exception.ErrorCode
import org.example.utils.MongoMethod
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class PriceServiceV1(
    private val mongoMethod: MongoMethod,
) {
    fun chartFromSymbol(symbol:String, excd : String, order : String, size: Int, page : Int) : Response<Chart> {
        try {
            val priceInfos: List<PriceInfo> = mongoMethod.findChartFromSymbol(symbol, excd, order, size, page)
            return Response.success(Chart(symbol, priceInfos))
        } catch (ex : Exception){
            throw CustomException(ErrorCode.FailedToGetChartData)
        }
    }
}