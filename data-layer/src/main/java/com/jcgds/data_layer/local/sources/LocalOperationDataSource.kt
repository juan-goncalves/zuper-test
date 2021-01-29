package com.jcgds.data_layer.local.sources

import com.jcgds.domain.entities.Message
import kotlinx.coroutines.flow.Flow

interface LocalOperationDataSource {

    val messageQueue: Flow<Message>

    suspend fun startOperation(id: String)

}