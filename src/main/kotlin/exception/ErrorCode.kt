package org.example.exception


enum class ErrorCode(
    override val code: Int,
    override var message: String
) : CodeInterface {
    FailedToConnectMongoDB(0, "Failed to connect MongoDB"),
    FailedToFindTemplate(1, "Failed to find template"),
}