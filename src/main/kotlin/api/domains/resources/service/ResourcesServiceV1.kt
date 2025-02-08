package org.example.api.resources.service

import lombok.RequiredArgsConstructor
import org.example.api.domains.resources.model.Resources
import org.example.api.protocol.Response
import org.example.exception.CustomException
import org.example.exception.ErrorCode
import org.example.utils.MongoMethod
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ResourcesServiceV1(
    private val mongoMethod: MongoMethod,
) {

    fun allResources() : Response<List<Resources>> {
        try {
            val result : List<Resources> = mongoMethod.findAllResources()
            return Response.success(result)
        } catch (e: Exception){
            throw CustomException(ErrorCode.FailedToGetAllResourcesData)
        }
    }

    fun resources(symbol : String) : Response<Resources> {
        try {
            val result : Resources? = mongoMethod.findResource(symbol)

            result?.let {
                return Response.success(result)
            } ?: run {
                throw CustomException(ErrorCode.NoResourcesData)
            }
        } catch (e: Exception){
            throw CustomException(ErrorCode.FailedToGetResourcesData)
        }
    }

}