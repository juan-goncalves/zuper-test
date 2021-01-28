package com.jcgds.domain.entities

data class Message(
    val operationId: String,
    val message: String,
    val progress: Int? = null,
    val state: String? = null
)