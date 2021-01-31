package com.jcgds.data_layer.sources

import com.jcgds.domain.entities.Message
import kotlinx.coroutines.flow.Flow

interface OperationRunner {

    val messageQueue: Flow<Message>

    val errorsStream: Flow<Exception>

    /** Ensures that the runner is ready to execute operations. */
    suspend fun initialize()

    /**
     * Enqueues an operation to be executed.
     * The OperationRunner must be initialized using the [initialize] method.
     * As the operation start is deferred, errors are published through the [errorsStream] field.
     * */
    suspend fun startOperation(id: String)

}