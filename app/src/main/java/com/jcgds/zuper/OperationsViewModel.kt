package com.jcgds.zuper

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jcgds.domain.entities.Operation
import com.jcgds.domain.repositories.OperationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class OperationsViewModel @ViewModelInject constructor(
    private val operationsRepository: OperationRepository,
) : ViewModel() {

    val operations: LiveData<List<Operation>> = operationsRepository.operationsStream
        .map { ops -> ops.toList() }
        .asLiveData()

    init {
        viewModelScope.launch {
            operationsRepository.initializeExecutor()
            startOperations(200)
        }
    }

    private suspend fun startOperations(amount: Int) = withContext(Dispatchers.Default) {
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        for (i in 1..amount) {
            val randomString = (1..7)
                .map { Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

            operationsRepository.startOperation(randomString)
        }
    }


}