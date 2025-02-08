package org.example.model.mapper

import org.example.api.prices.model.Chart
import org.example.api.prices.model.PriceInfo
import org.example.interfaces.Mapper
import org.example.model.api.PriceHistoryDoc

object ChartMapper : Mapper<List<PriceHistoryDoc>, List<PriceInfo>> {
    override fun map(input: List<PriceHistoryDoc>): List<PriceInfo> {
        return input.map { it ->
            PriceInfo(
                price = it.price,
                date = it.date
            )
        }
    }
}