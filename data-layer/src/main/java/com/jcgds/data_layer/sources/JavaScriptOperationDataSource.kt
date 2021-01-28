package com.jcgds.data_layer.sources

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.jcgds.data_layer.fakeJS
import com.jcgds.domain.entities.Message
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class JavaScriptOperationDataSource constructor(
    applicationContext: Context,
) : OperationDataSource {

    override val messageQueue: Flow<Message>
        get() = _messageQueue

    private val _messageQueue = MutableSharedFlow<Message>(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    private val bridge = object {
        @JavascriptInterface
        fun postMessage(message: String) {
            // TODO: Map message into Message object
            // TODO: Emit the message to the queue
            val emitted = _messageQueue.tryEmit(Message("wjefhj223", message, 0, ""))
            Log.d("JavaScriptOperationDS", "Received JS message: $message (relayed = $emitted)")
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private val webView = WebView(applicationContext).apply {
        settings.javaScriptEnabled = true
        addJavascriptInterface(bridge, "injectedObj")
        evaluateJavascript("window.jumbo = injectedObj;", null)
        evaluateJavascript(fakeJS, null)
    }

}