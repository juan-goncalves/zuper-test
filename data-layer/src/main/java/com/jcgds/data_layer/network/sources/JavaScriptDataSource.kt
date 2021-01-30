package com.jcgds.data_layer.network.sources

import com.jcgds.data_layer.network.services.ZuperAwsService

class JavaScriptDataSource constructor(
    private val jsService: ZuperAwsService
) {

    suspend fun getOperationsRunner(): String {
        val response = jsService.downloadOperationsJsFile()
        return response.charStream().readText()
    }

}