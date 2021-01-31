package com.jcgds.data_layer.repositories

import com.jcgds.data_layer.errors.ExceptionHandler
import com.jcgds.data_layer.sources.OperationRunner
import com.jcgds.domain.entities.Operation
import com.jcgds.domain.entities.Result
import com.jcgds.domain.repositories.OperationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OperationRepositoryImpl @Inject constructor(
    private val runner: OperationRunner,
    private val exceptionHandler: ExceptionHandler,
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

    override suspend fun enqueueOperation(id: String): Result<Unit> {
        return try {
            runner.startOperation(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            val message = exceptionHandler.handle(e)
            Result.Failure(message)
        }
    }

    override suspend fun initializeExecutor(): Result<Unit> {
        return try {
            runner.initialize()
            Result.Success(Unit)
        } catch (e: Exception) {
            val message = exceptionHandler.handle(e)
            Result.Failure(message)
        }
    }

}