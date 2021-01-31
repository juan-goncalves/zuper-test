package com.jcgds.data_layer.sources

import com.jcgds.domain.entities.Message
import kotlinx.coroutines.flow.Flow

interface OperationRunner {

    val messageQueue: Flow<Message>

    val errorsStream: Flow<Exception>

    suspend fun initialize()

    /**
     * Enqueues an operation to be started.
     * As the initialization is deferred, errors are published through the [errorsStream] field.
     * */
    suspend fun startOperation(id: String)

}