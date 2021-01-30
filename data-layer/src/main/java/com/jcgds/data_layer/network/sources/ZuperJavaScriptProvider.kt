package com.jcgds.data_layer.network.sources

import com.jcgds.data_layer.network.services.ZuperAwsService
import com.jcgds.data_layer.sources.JavaScriptProvider
import javax.inject.Inject

class ZuperJavaScriptProvider @Inject constructor(
    private val jsService: ZuperAwsService
) : JavaScriptProvider {

    override suspend fun getOperationsRunner(): String {
        val response = jsService.downloadOperationsJsFile()
        return response.charStream().readText()
    }

}