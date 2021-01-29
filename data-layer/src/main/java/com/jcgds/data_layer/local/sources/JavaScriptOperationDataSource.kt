package com.jcgds.data_layer.local.sources

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.jcgds.data_layer.local.models.MessageSchema
import com.jcgds.data_layer.local.models.toDomain
import com.jcgds.data_layer.network.services.ZuperAwsService
import com.jcgds.domain.entities.Message
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext

class JavaScriptOperationDataSource constructor(
    applicationContext: Context,
    private val zuperService: ZuperAwsService
) : LocalOperationDataSource {

    private var initialized = false

    override val messageQueue: Flow<Message>
        get() = _messageQueue

    private val _messageQueue: MutableSharedFlow<Message> = MutableSharedFlow(
        extraBufferCapacity = Channel.UNLIMITED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private suspend fun initializeIfNecessary() {
        if (initialized) return

        val code = getOperationsJsCode()
        webView.evaluateJavascript(code, null)
        initialized = true
    }

    private val adapter = Moshi.Builder().build().adapter(MessageSchema::class.java)

    private val bridge = object {
        @JavascriptInterface
        fun postMessage(messageStr: String) {
            Log.d("JavaScriptOperationDS", "Received message from JS: $messageStr")
            val message = adapter.fromJson(messageStr)?.toDomain()
            if (message != null) {
                val emitted = _messageQueue.tryEmit(message)
                Log.d(
                    "JavaScriptOperationDS",
                    "Parse JS message: $message (relayed = $emitted)"
                )
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private val webView = WebView(applicationContext).apply {
        settings.javaScriptEnabled = true
        addJavascriptInterface(bridge, "injectedObj")
        evaluateJavascript("window.jumbo = injectedObj;", null)
    }

    override suspend fun startOperation(id: String) = withContext(Dispatchers.Main) {
        initializeIfNecessary()
        webView.evaluateJavascript(
            "startOperation('$id');",
            null,
        )
    }

    private suspend fun getOperationsJsCode(): String {
        val response = zuperService.downloadOperationsJsFile()
        return response.charStream().readText()
    }

}