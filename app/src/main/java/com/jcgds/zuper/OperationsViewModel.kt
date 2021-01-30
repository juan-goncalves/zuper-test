package com.jcgds.zuper

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.jcgds.domain.entities.Operation
import com.jcgds.domain.entities.Result
import com.jcgds.domain.repositories.OperationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class OperationsViewModel @ViewModelInject constructor(
    private val operationsRepository: OperationRepository,
) : ViewModel() {

    val operations: LiveData<List<Operation>> = operationsRepository.operationsStream.asLiveData()

    val showErrorState: LiveData<Boolean> get() = _showErrorState
    private val _showErrorState: MutableLiveData<Boolean> = MutableLiveData(false)

    val showLoadingState: LiveData<Boolean> get() = _showLoadingState
    private val _showLoadingState: MutableLiveData<Boolean> = MutableLiveData(true)

    init {
        viewModelScope.launch {
            _showLoadingState.postValue(true)
            when (operationsRepository.initializeExecutor()) {
                is Result.Success -> startOperations(200)
                is Result.Failure -> _showErrorState.postValue(true)
            }
            _showLoadingState.postValue(false)
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