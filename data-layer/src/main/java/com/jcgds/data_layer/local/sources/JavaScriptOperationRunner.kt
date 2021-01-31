package com.jcgds.data_layer.local.sources

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.jcgds.data_layer.errors.JavaScriptException
import com.jcgds.data_layer.local.models.MessageModel
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

    override val errorsStream: Flow<Exception>
        get() = _errorsStream

    override val messageQueue: Flow<Message>
        get() = _messageQueue

    private val _messageQueue: MutableSharedFlow<Message> = MutableSharedFlow(
        extraBufferCapacity = Channel.UNLIMITED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val _errorsStream: MutableSharedFlow<Exception> = MutableSharedFlow(
        extraBufferCapacity = Channel.UNLIMITED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override suspend fun initialize() {
        if (initialized) return

        val code = jsProvider.getOperationsRunner()
        withContext(Dispatchers.Main) {
            webView.evaluateJavascript(code.catchingExceptionsInBridge(), null)
        }

        initialized = true
    }

    private val adapter = moshi.adapter(MessageModel::class.java)

    private val bridge = object {
        @JavascriptInterface
        fun postMessage(messageStr: String) {
            val message = adapter.fromJson(messageStr)?.toDomain()
            if (message != null) {
                _messageQueue.tryEmit(message)
            } else {
                Log.w("JsOperationRunner", "Invalid message received: $messageStr")
            }
        }

        @JavascriptInterface
        fun processError(errorName: String?, message: String?) {
            _errorsStream.tryEmit(JavaScriptException(message))
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private val webView = WebView(context).apply {
        settings.javaScriptEnabled = true
        addJavascriptInterface(bridge, "__bridge")
        evaluateJavascript("window.jumbo = __bridge;", null)
    }

    override suspend fun startOperation(id: String) {
        require(initialized) { "The operations runner has not been initialized" }

        withContext(Dispatchers.Main) {
            webView.evaluateJavascript(
                "startOperation('$id');".catchingExceptionsInBridge(),
                null,
            )
        }
    }

    private fun String.catchingExceptionsInBridge(): String {
        return """
            try {
                $this
            } catch (e) {
                __bridge.processError(e.name, e.message);
            }
        """.trimIndent()
    }

}

