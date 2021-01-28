package com.jcgds.data_layer.schemas

import com.jcgds.domain.entities.Message
import com.jcgds.domain.entities.Operation
import com.squareup.moshi.Json

data class MessageSchema(
    @field:Json(name = "id") val operationId: String,
    @field:Json(name = "message") val message: String,
    @field:Json(name = "progress") val progress: Int? = null,
    @field:Json(name = "state") val state: String? = null
)

fun MessageSchema.toDomain(): Message = Message(
    operationId,
    message,
    progress ?: 100,
    state.toOperationState()
)

fun String?.toOperationState(): Operation.State {
    return when (this) {
        "success" -> Operation.State.Success
        "error" -> Operation.State.Error
        else -> Operation.State.Unknown
    }
}