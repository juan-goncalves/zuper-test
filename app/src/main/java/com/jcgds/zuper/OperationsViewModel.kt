package com.jcgds.zuper

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jcgds.domain.entities.Operation
import com.jcgds.domain.repositories.OperationRepository
import kotlinx.coroutines.flow.map

class OperationsViewModel @ViewModelInject constructor(
    operationsRepository: OperationRepository,
) : ViewModel() {

    val operations: LiveData<List<Operation>> = operationsRepository.operationsStream
        .map { ops -> ops.toList() }
        .asLiveData()

}