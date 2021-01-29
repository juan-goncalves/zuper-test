package com.jcgds.domain.repositories

import com.jcgds.domain.entities.Result

interface JavaScriptRepository {

    suspend fun getJavaScriptOperationsSourceCode(): Result<String>

}