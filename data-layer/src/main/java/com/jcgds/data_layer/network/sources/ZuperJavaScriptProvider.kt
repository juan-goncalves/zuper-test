package com.jcgds.data_layer.network.sources

import com.jcgds.data_layer.network.services.ZuperAwsService
import com.jcgds.data_layer.sources.JavaScriptProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ZuperJavaScriptProvider @Inject constructor(
    private val zuperService: ZuperAwsService
) : JavaScriptProvider {

    override suspend fun getOperationsRunner(): String {
        val response = zuperService.downloadOperationsJsFile()
        val stream = response.charStream()
        val result = stream.readText()
        withContext(Dispatchers.IO) { stream.close() }
        return result
    }

}