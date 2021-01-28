package com.jcgds.zuper

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jcgds.data_layer.repositories.FakeOperationRepository
import com.jcgds.domain.entities.Operation
import com.jcgds.domain.repositories.OperationRepository
import kotlinx.coroutines.flow.map

class OperationsViewModel : ViewModel() {

    // TODO: DI
    private val operationsRepository: OperationRepository = FakeOperationRepository()

    val operations: LiveData<List<Operation>> = operationsRepository.operationsStream
        .map { ops -> ops.toList().sortedBy { it.id } }
        .asLiveData()

}