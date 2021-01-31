package com.jcgds.data_layer.sources

interface JavaScriptProvider {

    suspend fun getOperationsRunner(): String

}