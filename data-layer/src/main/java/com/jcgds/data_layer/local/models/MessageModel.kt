package com.jcgds.data_layer.local.models

import com.jcgds.domain.entities.Message
import com.jcgds.domain.entities.Operation
import com.squareup.moshi.Json

data class MessageModel(
    @field:Json(name = "id") val operationId: String?,
    @field:Json(name = "message") val message: String?,
    @field:Json(name = "progress") val progress: Int? = null,
    @field:Json(name = "state") val state: String? = null
)

fun MessageModel.toDomain(): Message? {
    val operationState = when (message) {
        "progress" -> Operation.State.Ongoing
        "completed" -> when (state) {
            "error" -> Operation.State.Completed.Error
            "success" -> Operation.State.Completed.Success
            else -> null
        }
        else -> null
    }

    val operationProgress = when {
        operationState is Operation.State.Completed -> 100
        progress == null || progress < 0 && operationState == Operation.State.Ongoing -> return null
        else -> progress
    }

    return Message(
        operationId ?: return null,
        operationState ?: return null,
        operationProgress,
    )
}
