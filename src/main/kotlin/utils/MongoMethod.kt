package org.example.utils

import com.mongodb.bulk.BulkWriteResult
import com.mongodb.internal.bulk.WriteRequest
import lombok.RequiredArgsConstructor
import org.example.advice.TxAdvice
import org.example.api.domains.resources.model.Resources
import org.example.api.prices.model.PriceInfo
import org.example.exception.CustomException
import org.example.exception.ErrorCode
import org.example.model.api.Output2
import org.example.model.api.PriceHistoryDoc
import org.example.model.api.TokenIssueResponse
import org.example.model.enums.MongoTableCollector
import org.example.model.mapper.ChartMapper
import org.example.model.mapper.OutPut2Checker
import org.example.repository.mongo.QueryBuilder
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Collation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
@RequiredArgsConstructor
class MongoMethod(
    private val template: HashMap<String, MongoTemplate>,
) {

    fun findChartFromSymbol(
        symbol:String,
        excd: String,
        order:String,
        page : Int,
        size : Int
    ) : List<PriceInfo> = TxAdvice.readOnly {
        val query = Query()

        query.addCriteria(Criteria.where("excd").`is`(excd))
        query.addCriteria(Criteria.where("symbol").`is`(symbol))

        val o :String = order.lowercase()

        if (o == "asc") {
            query.with(Sort.by(Sort.Order.asc("date")))
        }else {
            query.with(Sort.by(Sort.Order.desc("date")))
        }

        query.collation(Collation.of("en").numericOrdering(true))
        query.with(PageRequest.of(page, size))

        val template : MongoTemplate = mongoTemplate(MongoTableCollector.MarketInsight)
        val result : List<PriceHistoryDoc>  = template.find(query, PriceHistoryDoc::class.java)

        return@readOnly ChartMapper.map(result)
    }

    fun findAllVolume(
        symbol: String,
        excd : String,
        startDate : String,
        endDate : String
    ) : String = TxAdvice.readOnly {
        // -> 파이프라인을 사용해도 되지만, DB에 대해서 많은 연산을 하게 하고 싶지가 않기 떄문에, client에서 계산
        //        val aggregation = Aggregation.newAggregation(
        //            Aggregation.match(
        //                Criteria.where("excd").`is`(excd)
        //                    .and("symbol").`is`(symbol)
        //                    .and("date").gte(startDate).lte(endDate)
        //            ),
        //            Aggregation.group()  // 모든 결과를 그룹화 (그룹핑 기준 없이 모든 데이터 합산)
        //                .sum("price").`as`("totalPrice")  // price 필드를 합산하여 totalPrice라는 필드로 생성
        //        )

        val query = Query()

        query.addCriteria(Criteria.where("excd").`is`(excd))
        query.addCriteria(Criteria.where("symbol").`is`(symbol))
        query.collation(Collation.of("en").numericOrdering(true))

        query.addCriteria(Criteria.where("date").gte(startDate).lte(endDate))

        val template : MongoTemplate = mongoTemplate(MongoTableCollector.MarketInsight)
        val results: List<PriceHistoryDoc> = template.find(query, PriceHistoryDoc::class.java)

        val totalPrice = results.fold(BigDecimal.ZERO) { sum, doc ->
            sum + doc.price.toBigDecimal()
        }

        return@readOnly totalPrice.toString()
    }

    fun findKeyHistory(): TokenIssueResponse? = TxAdvice.readOnly {
        val pageable: Pageable? = null
        val criteriaMap: Map<String, Any>? = null
        val sortOrders = listOf(
            Sort.Order.desc("accessTokenExpired"),
        )

        val query = QueryBuilder.createQuery(pageable, criteriaMap, sortOrders)
        val template : MongoTemplate = mongoTemplate(MongoTableCollector.MarketInsight)

        return@readOnly QueryBuilder.findOne(template, query, TokenIssueResponse::class.java)
    }

    fun updateKeyHistory(data: TokenIssueResponse) : Boolean = TxAdvice.run {

        val template : MongoTemplate = mongoTemplate(MongoTableCollector.MarketInsight)

        val update = Update()
            .set("accessToken", data.accessToken)
            .set("accessTokenExpired", data.accessTokenTokenExpired)
            .set("tokenType", data.tokenType)
            .set("expiresIn", data.expiresIn)
        val query = Query()


        return@run QueryBuilder.update(template,query,  update, TokenIssueResponse::class.java)
    }

    fun findResource(symbol : String) : Resources? = TxAdvice.readOnly {
        val template : MongoTemplate = mongoTemplate(MongoTableCollector.MarketInsight)

        val query = Query()
        query.addCriteria(Criteria.where("symbol").`is`(symbol))

        return@readOnly QueryBuilder.findOne(template, query, Resources::class.java)
    }

    fun findAllResources(): List<Resources>  = TxAdvice.readOnly {
        val template : MongoTemplate = mongoTemplate(MongoTableCollector.MarketInsight)

        return@readOnly QueryBuilder.find(template, Query(), Resources::class.java)
    }

    fun findLatestPriceHistory(symbol : String, excd : String): PriceHistoryDoc? = TxAdvice.readOnly {
        val template : MongoTemplate = mongoTemplate(MongoTableCollector.MarketInsight)

        val query = Query()

        query.addCriteria(Criteria.where("excd").`is`(excd))
        query.addCriteria(Criteria.where("symbol").`is`(symbol))

        query.with(Sort.by(Sort.Order.desc("date")))
        query.collation(Collation.of("en").numericOrdering(true))

        return@readOnly QueryBuilder.findOne(template, query, PriceHistoryDoc::class.java)
    }

    fun upsertPriceHistory(
        symbol: String,
        excd: String,
        latestDocDate : String,
        priceHistory: List<Output2>
    ): BulkWriteResult = TxAdvice.run {
        val template: MongoTemplate = mongoTemplate(MongoTableCollector.MarketInsight)
        val bulkOps: BulkOperations = template.bulkOps(BulkOperations.BulkMode.ORDERED, PriceHistoryDoc::class.java)

        // hasOperations는 단 한번만 접근하게
        val hasOperations: Boolean by lazy {
            priceHistory.any { price ->

                val needAdded: Boolean = OutPut2Checker.check(latestDocDate, price.xymd)

                when {
                    needAdded -> {
                        val query = Query(
                            Criteria.where("symbol").`is`(symbol)
                                .and("date").`is`(price.xymd)
                                .and("excd").`is`(excd)
                        )

                        val update = Update()
                            .set("symbol", symbol)
                            .set("excd", excd)
                            .set("date", price.xymd)
                            .set("price", price.clos)

                        bulkOps.upsert(query, update)  // 작업 추가
                        true
                    }
                    else -> false
                }
            }
        }

        return@run when {
            hasOperations -> bulkOps.execute()  // 작업이 있을 경우 실행
            else -> BulkWriteResult.acknowledged(WriteRequest.Type.UPDATE, 0, 0, emptyList(), emptyList())  // 작업이 없으면 빈 결과 반환
        }
    }

    private fun mongoTemplate(c: MongoTableCollector) : MongoTemplate {
        val table = template[c.table]

        table?.let { return it }

        throw CustomException(ErrorCode.FailedToFindTemplate)
    }
}