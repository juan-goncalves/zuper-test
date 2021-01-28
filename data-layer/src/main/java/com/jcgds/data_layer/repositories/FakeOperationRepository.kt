package com.jcgds.data_layer.repositories

import com.jcgds.domain.entities.Operation
import com.jcgds.domain.repositories.OperationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeOperationRepository : OperationRepository {

    override val operationsStream: Flow<Set<Operation>>
        get() = flowOf()

    override suspend fun startOperation(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateOperationProgress(id: String, progress: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun finishOperation(id: String) {
        TODO("Not yet implemented")
    }

}