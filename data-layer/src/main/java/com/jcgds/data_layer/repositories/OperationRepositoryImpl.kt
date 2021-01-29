package com.jcgds.data_layer.repositories

import com.jcgds.data_layer.local.sources.LocalOperationDataSource
import com.jcgds.domain.entities.Operation
import com.jcgds.domain.repositories.OperationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OperationRepositoryImpl constructor(
    private val localOperationDs: LocalOperationDataSource,
) : OperationRepository {

    private val operationsLookup: MutableMap<String, Operation> = hashMapOf()
    private val operations: MutableSet<Operation> = hashSetOf()

    override val operationsStream: Flow<Set<Operation>>
        get() = localOperationDs.messageQueue.map { message ->
            val operation = operationsLookup[message.operationId]
            val updatedOp = operation?.copy(state = message.state, progress = message.progress)
                ?: Operation(message.operationId, message.state, message.progress)

            if (operation != null) operations.remove(operation)
            operations.add(updatedOp)
            operationsLookup[message.operationId] = updatedOp
            operations
        }

    override suspend fun startOperation(id: String) {
        localOperationDs.startOperation(id)
    }

}