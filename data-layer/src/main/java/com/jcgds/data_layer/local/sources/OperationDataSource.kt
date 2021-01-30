package com.jcgds.data_layer.local.sources

import com.jcgds.domain.entities.Message
import kotlinx.coroutines.flow.Flow

interface OperationDataSource {

    val messageQueue: Flow<Message>

    suspend fun initializeExecutor()

    suspend fun startOperation(id: String)

}