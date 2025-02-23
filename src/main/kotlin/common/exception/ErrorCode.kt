package org.example.exception


enum class ErrorCode(
    override val code: Int,
    override var message: String
) : CodeInterface {
    FailedToConnectMongoDB(0, "Failed to connect MongoDB"),
    FailedToFindTemplate(1, "Failed to find template"),
    FailedToGetKeyHistoryFromMongo(3, "Failed to get key history"),
    ExceptionError(4, "err occurred"),
    FailedToCallClient(5, "Failed to call client : "),
    FailedToSetKeyHistory(6, "Failed to set key history"),


    FailedToGetChartData(7, "Failed to get chart data"),
    FailedToGetAllVolume(8, "Failed to get all volume info"),
    NotSupportedOrderRequest(9, "Failed to get order request"),
    StartDateLowerThanEndDate(10, "startDate must be bigger than endDate"),
    NoResourcesData(11, "no resource data"),
    FailedToGetResourcesData(12, "failed to get resources"),
    FailedToGetAllResourcesData(13, "failed to get all resources"),
}