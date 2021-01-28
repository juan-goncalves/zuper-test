package com.jcgds.data_layer.sources

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.jcgds.data_layer.fakeJS
import com.jcgds.data_layer.schemas.MessageSchema
import com.jcgds.data_layer.schemas.toDomain
import com.jcgds.domain.entities.Message
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
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

    private val moshi = Moshi.Builder().build()
    private val adapter: JsonAdapter<MessageSchema> = moshi.adapter(MessageSchema::class.java)

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
        evaluateJavascript(fakeJS, null)
    }

    fun dispose() {
        webView.destroy()
    }

}