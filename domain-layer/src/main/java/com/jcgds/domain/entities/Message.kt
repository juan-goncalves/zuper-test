package com.jcgds.domain.entities

data class Message(
    val operationId: String,
    val message: String,
    val progress: Int,
    val state: Operation.State
)