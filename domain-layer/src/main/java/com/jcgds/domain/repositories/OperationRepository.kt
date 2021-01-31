package com.jcgds.domain.repositories

import com.jcgds.domain.entities.Operation
import com.jcgds.domain.entities.Result
import kotlinx.coroutines.flow.Flow

interface OperationRepository {

    val operationsStream: Flow<List<Operation>>

    suspend fun initializeExecutor(): Result<Unit>

    suspend fun enqueueOperation(id: String): Result<Unit>

}