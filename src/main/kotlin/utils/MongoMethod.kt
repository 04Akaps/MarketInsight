package org.example.utils

import lombok.RequiredArgsConstructor
import org.example.exception.CustomException
import org.example.exception.ErrorCode
import org.example.model.api.TokenIssueResponse
import org.example.model.enums.MongoTableCollector
import org.example.repository.mongo.QueryBuilder
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class MongoMethod(
    private val template: HashMap<String, MongoTemplate>
) {

    fun test() : String {
        return "serser"
    }

    fun findKeyHistory(): TokenIssueResponse? {
        val pageable: Pageable? = null
        val criteriaMap: Map<String, Any>? = null
        val sortOrders = listOf(
            Sort.Order.desc("accessTokenExpired"),
        )

        val query = QueryBuilder.createQuery(pageable, criteriaMap, sortOrders)
        val template : MongoTemplate = mongoTemplate(MongoTableCollector.MarketInsight)

        return QueryBuilder.findOne(template, query, TokenIssueResponse::class.java)
    }

    fun updateKeyHistory(data: TokenIssueResponse) : Boolean {

        val template : MongoTemplate = mongoTemplate(MongoTableCollector.MarketInsight)

        val update = Update()
            .set("accessToken", data.accessToken)
            .set("accessTokenExpired", data.accessTokenTokenExpired)
            .set("tokenType", data.tokenType)
            .set("expiresIn", data.expiresIn)
        val query = Query()


        return QueryBuilder.update(template,query,  update, TokenIssueResponse::class.java)
    }

    private fun mongoTemplate(c: MongoTableCollector) : MongoTemplate {
        val table = template[c.table]

        table?.let { return it }

        throw CustomException(ErrorCode.FailedToFindTemplate)
    }
}