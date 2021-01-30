package com.jcgds.data_layer.repositories

import com.jcgds.data_layer.sources.OperationRunner
import com.jcgds.domain.entities.Operation
import com.jcgds.domain.entities.Result
import com.jcgds.domain.repositories.OperationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OperationRepositoryImpl constructor(
    private val runner: OperationRunner,
) : OperationRepository {

    private val operations: MutableMap<String, Operation> = LinkedHashMap()

    override val operationsStream: Flow<List<Operation>>
        get() = runner.messageQueue.map { message ->
            val operation = operations[message.operationId]
            val updatedOp = operation?.copy(state = message.state, progress = message.progress)
                ?: Operation(message.operationId, message.state, message.progress)

            operations[message.operationId] = updatedOp
            operations.values.toList()
        }

    override suspend fun startOperation(id: String) {
        // TODO: Handle exceptions and wrap with Result<>
        runner.startOperation(id)
    }

    override suspend fun initializeExecutor(): Result<Unit> {
        // TODO: Handle exceptions and wrap with Result<>
        runner.initialize()
        return Result.Success(Unit)
    }

}