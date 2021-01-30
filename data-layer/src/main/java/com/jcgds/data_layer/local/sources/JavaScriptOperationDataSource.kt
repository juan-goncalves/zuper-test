package com.jcgds.data_layer.local.sources

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.jcgds.data_layer.local.models.MessageSchema
import com.jcgds.data_layer.local.models.toDomain
import com.jcgds.data_layer.network.sources.JavaScriptDataSource
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
    private val jsProvider: JavaScriptDataSource,
) : OperationDataSource {

    private var runnerHasBeenInitialized = false

    override val messageQueue: Flow<Message>
        get() = _messageQueue

    private val _messageQueue: MutableSharedFlow<Message> = MutableSharedFlow(
        extraBufferCapacity = Channel.UNLIMITED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override suspend fun initializeExecutor() {
        if (runnerHasBeenInitialized) return

        val code = jsProvider.getOperationsRunner()
        webView.evaluateJavascript(code, null)
        runnerHasBeenInitialized = true
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
        require(runnerHasBeenInitialized) { "The operations runner has not been initialized" }

        webView.evaluateJavascript(
            "startOperation('$id');",
            null,
        )
    }

}