package org.example.api.resources.service

import lombok.RequiredArgsConstructor
import org.example.utils.MongoMethod
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ResourcesServiceV1(
    private val mongoMethod: MongoMethod,
) {

}