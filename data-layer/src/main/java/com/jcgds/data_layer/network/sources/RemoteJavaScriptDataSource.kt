package com.jcgds.data_layer.network.sources

import com.jcgds.data_layer.network.services.ZuperAwsService

class RemoteJavaScriptDataSource constructor(
    private val jsService: ZuperAwsService
) {

    suspend fun getOperationsJsCode(): String {
        val response = jsService.downloadOperationsJsFile()
        return response.charStream().readText()
    }

}