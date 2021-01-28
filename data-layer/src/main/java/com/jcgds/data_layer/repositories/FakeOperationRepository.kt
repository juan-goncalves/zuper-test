package com.jcgds.data_layer.repositories

import android.util.Log
import com.jcgds.data_layer.sources.OperationDataSource
import com.jcgds.domain.entities.Operation
import com.jcgds.domain.repositories.OperationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FakeOperationRepository constructor(
    private val operationDataSource: OperationDataSource
) : OperationRepository {

    private val operations: MutableMap<String, Operation> = hashMapOf()

    override val operationsStream: Flow<Set<Operation>>
        get() = operationDataSource.messageQueue.map { message ->
            Log.d("FakeOperationRepo", "Received message: $message")
            val operation = operations[message.operationId]
            val updatedOperation = if (operation == null) {
                Operation(message.operationId, message.state ?: "none", 0)
            } else {
                operation.copy(state = message.state!!, progress = message.progress!!)
            }

            // TODO: Improve perf
            operations[message.operationId] = updatedOperation
            operations.values.toSet()
        }


    override suspend fun startOperation(id: String) {
        TODO("Not yet implemented")
    }

}