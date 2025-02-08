package org.example.api.resources.controller

import lombok.RequiredArgsConstructor
import org.example.api.domains.resources.model.Resources
import org.example.api.protocol.Response
import org.example.api.resources.service.ResourcesServiceV1
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/resource")
@RequiredArgsConstructor
class ResourcesControllerV1(
    private val resourcesService: ResourcesServiceV1
) {

    @GetMapping("/all-resources")
    fun allResources() : Response<List<Resources>> {
        return resourcesService.allResources()
    }

    @GetMapping("/resource/{symbol}")
    fun resource(
        @PathVariable symbol: String,
    ) : Response<Resources> {
        return resourcesService.resources(symbol)
    }
}