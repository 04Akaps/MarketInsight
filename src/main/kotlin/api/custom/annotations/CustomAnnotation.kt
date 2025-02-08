package org.example.api.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class PageCustomAnnotation(val defaultPage: Int = 1)


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class SizeCustomAnnotation(val defaultSize: Int = 50)


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class OrderCustomAnnotation(val defaultSort: String = "DESC")