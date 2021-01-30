package com.jcgds.domain.repositories

import com.jcgds.domain.entities.Operation
import kotlinx.coroutines.flow.Flow

interface OperationRepository {

    val operationsStream: Flow<List<Operation>>

    suspend fun initializeExecutor()

    // TODO: return Result<Operation>
    suspend fun startOperation(id: String)

}