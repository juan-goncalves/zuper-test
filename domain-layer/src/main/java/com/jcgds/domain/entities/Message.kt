package com.jcgds.domain.entities

data class Message(
    val operationId: String,
    val operationState: Operation.State,
    val progress: Int,
)

fun Message.toOperation(): Operation = Operation(operationId, operationState, progress)
