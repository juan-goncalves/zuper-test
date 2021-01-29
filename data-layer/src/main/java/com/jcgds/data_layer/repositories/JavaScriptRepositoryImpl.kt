package com.jcgds.data_layer.repositories

import com.jcgds.data_layer.network.services.StaticJavascriptService
import com.jcgds.domain.entities.Result
import com.jcgds.domain.repositories.JavaScriptRepository

class JavaScriptRepositoryImpl constructor(
    private val remoteDs: StaticJavascriptService
) : JavaScriptRepository {

    override suspend fun getJavaScriptOperationsSourceCode(): Result<String> {
        TODO()
    }

}