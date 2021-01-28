package com.jcgds.data_layer.schemas

import com.jcgds.domain.entities.Message
import com.squareup.moshi.Json

data class MessageSchema(
    @field:Json(name = "id") val operationId: String,
    @field:Json(name = "message") val message: String,
    @field:Json(name = "progress") val progress: Int? = null,
    @field:Json(name = "state") val state: String? = null
)

fun MessageSchema.toDomain(): Message = Message(operationId, message, progress ?: -1, state ?: "_")
