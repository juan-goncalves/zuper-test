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
import kotlin.random.Random

class OperationsViewModel @ViewModelInject constructor(
    private val operationsRepository: OperationRepository,
) : ViewModel() {

    val operations: LiveData<List<Operation>> = operationsRepository.operationsStream
        .map { ops -> ops.toList() }
        .asLiveData()

    init {
        startOperations(200)
    }

    private fun startOperations(amount: Int) {
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        viewModelScope.launch(Dispatchers.Default) {
            for (i in 1..amount) {
                val randomString = (1..7)
                    .map { Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")

                operationsRepository.startOperation(randomString)
            }
        }
    }

}