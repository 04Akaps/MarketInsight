package org.example.repository.mongo

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Collation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update


object QueryBuilder {
    fun pageable(page: Int, size: Int): Pageable {
        return PageRequest.of(page, size)
    }

    fun createQuery(pageable: Pageable?, criteriaMap: Map<String, Any>?, sort: List<Sort.Order>?): Query {
        val query = Query()

        pageable?.let { query.with(pageable) }

        criteriaMap?.let {
            for ((key, value) in criteriaMap) {
                query.addCriteria(Criteria.where(key).`is`(value))
            }
        }

        sort?.let {
            query.with(Sort.by(it))
        }

        return query
    }

    fun <T> findOne(template: MongoTemplate, query: Query, targetClass: Class<T>): T? {
        return template.findOne(query, targetClass)
    }

    fun <T> find(template: MongoTemplate, query: Query, targetClass: Class<T>): List<T> {
        return template.find(query, targetClass)
    }

    fun createUpdate(updateMap: Map<String, Any>): Update {
        val update = Update()
        for ((key, value) in updateMap) {
            update[key] = value
        }
        return update
    }

    fun <T> findAndModify(template: MongoTemplate, query: Query, update: Update, targetClass: Class<T>): T? {
        return template.findAndModify(query, update, targetClass)
    }

    fun <T> findAndModify(
        template: MongoTemplate,
        query: Query,
        update: Update,
        targetClass: Class<T>,
        upsert: Boolean
    ): T? {
        val options = FindAndModifyOptions.options().returnNew(true)
        if (upsert) {
            options.upsert(true)
        }
        return template.findAndModify(query, update, options, targetClass)
    }

    fun <T> update(template: MongoTemplate, query: Query, update: Update, targetClass: Class<T>): Boolean {
        val updateResult = template.updateFirst(query, update, targetClass)
        return updateResult.modifiedCount > 0
    }




    fun addProjection(query: Query, fields: List<String>): Query {

        val projectionFields = query.fields()

        for (field in fields) {
            projectionFields.include(field)
        }


        return query
    }



    fun addSort(query: Query, sortFields: Map<String, Sort.Direction>?): Query {
        sortFields?.let {
            for ((key, value) in sortFields) {
                query.with(Sort.by(value, key))
            }
        }

        return query
    }


    private fun addNumericOrdering(query: Query): Query {
        return query.collation(Collation.of("en").numericOrderingEnabled())
    }
}