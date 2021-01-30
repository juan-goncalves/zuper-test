package com.jcgds.data_layer.sources

import com.jcgds.domain.entities.Message
import kotlinx.coroutines.flow.Flow

interface OperationRunner {

    val messageQueue: Flow<Message>

    suspend fun initialize()

    suspend fun startOperation(id: String)

}