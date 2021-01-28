package com.jcgds.domain.repositories

import com.jcgds.domain.entities.Operation
import kotlinx.coroutines.flow.Flow

interface OperationRepository {

    val operationsStream: Flow<Set<Operation>>

    suspend fun startOperation(id: String)

}