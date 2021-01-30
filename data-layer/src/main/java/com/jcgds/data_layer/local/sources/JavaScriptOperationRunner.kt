package com.jcgds.data_layer.local.sources

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.jcgds.data_layer.local.models.MessageSchema
import com.jcgds.data_layer.local.models.toDomain
import com.jcgds.data_layer.sources.JavaScriptProvider
import com.jcgds.data_layer.sources.OperationRunner
import com.jcgds.domain.entities.Message
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class JavaScriptOperationRunner @Inject constructor(
    @ApplicationContext context: Context,
    moshi: Moshi,
    private val jsProvider: JavaScriptProvider,
) : OperationRunner {

    private var initialized = false

    override val messageQueue: Flow<Message>
        get() = _messageQueue

    private val _messageQueue: MutableSharedFlow<Message> = MutableSharedFlow(
        extraBufferCapacity = Channel.UNLIMITED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override suspend fun initialize() {
        if (initialized) return

        val code = jsProvider.getOperationsRunner()
        webView.evaluateJavascript(code, null)
        initialized = true
    }

    private val adapter = moshi.adapter(MessageSchema::class.java)

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
    private val webView = WebView(context).apply {
        settings.javaScriptEnabled = true
        addJavascriptInterface(bridge, "injectedObj")
        evaluateJavascript("window.jumbo = injectedObj;", null)
    }

    override suspend fun startOperation(id: String) = withContext(Dispatchers.Main) {
        require(initialized) { "The operations runner has not been initialized" }

        webView.evaluateJavascript(
            "startOperation('$id');",
            null,
        )
    }

}