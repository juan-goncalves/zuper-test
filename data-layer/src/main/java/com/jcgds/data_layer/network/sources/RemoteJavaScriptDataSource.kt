package com.jcgds.data_layer.network.sources

import com.jcgds.data_layer.network.services.StaticJavascriptService

class RemoteJavaScriptDataSource constructor(
    private val jsService: StaticJavascriptService
) {

    suspend fun getOperationsJsCode(): String {
        val response = jsService.downloadOperationsJsFile()
        return response.charStream().readText()
    }

}