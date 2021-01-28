package com.jcgds.zuper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.jcgds.data_layer.repositories.FakeOperationRepository
import com.jcgds.data_layer.sources.JavaScriptOperationDataSource
import com.jcgds.domain.entities.Operation
import com.jcgds.domain.repositories.OperationRepository
import kotlinx.coroutines.flow.map

class OperationsViewModel(application: Application) : AndroidViewModel(application) {

    // TODO: DI
    private var ds: JavaScriptOperationDataSource? =
        JavaScriptOperationDataSource(application.applicationContext)

    private val operationsRepository: OperationRepository = FakeOperationRepository(ds!!)

    val operations: LiveData<List<Operation>> = operationsRepository.operationsStream
        .map { ops -> ops.toList() }
        .asLiveData()

    override fun onCleared() {
        super.onCleared()
        ds = null
    }

}