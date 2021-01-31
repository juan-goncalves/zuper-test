package com.jcgds.data_layer.di

import com.jcgds.data_layer.local.sources.WebViewJsOperationRunner
import com.jcgds.data_layer.network.sources.ZuperJavaScriptProvider
import com.jcgds.data_layer.sources.JavaScriptProvider
import com.jcgds.data_layer.sources.OperationRunner
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindJavaScriptProvider(impl: ZuperJavaScriptProvider): JavaScriptProvider

    @Binds
    abstract fun bindOperationRunner(impl: WebViewJsOperationRunner): OperationRunner

}