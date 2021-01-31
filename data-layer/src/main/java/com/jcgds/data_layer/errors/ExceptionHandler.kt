package com.jcgds.data_layer.errors

import java.io.IOException
import javax.inject.Inject


class ExceptionHandler @Inject constructor() {

    // This error messages could be moved into a `StringProvider` class that handled localization
    private companion object ErrorMessages {
        const val Unknown = "An error occurred."
        const val Network = "You don't seem to have an active internet connection."
    }

    // In this method we could also handle logging to external services
    fun handle(exception: Exception): String {
        return when (exception) {
            is IOException -> Network
            is JavaScriptException -> exception.message ?: Unknown
            else -> Unknown
        }
    }

}