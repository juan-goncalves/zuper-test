package com.jcgds.domain.entities

data class Message(
    val operationId: String,
    val operationState: Operation.State,
    val progress: Int,
)