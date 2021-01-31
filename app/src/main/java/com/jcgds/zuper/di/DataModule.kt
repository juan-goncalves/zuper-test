package com.jcgds.zuper.di

import com.jcgds.data_layer.local.sources.JavaScriptOperationRunner
import com.jcgds.data_layer.network.sources.ZuperJavaScriptProvider
import com.jcgds.data_layer.repositories.OperationRepositoryImpl
import com.jcgds.data_layer.sources.JavaScriptProvider
import com.jcgds.data_layer.sources.OperationRunner
import com.jcgds.domain.repositories.OperationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindOperationRepository(impl: OperationRepositoryImpl): OperationRepository

    @Binds
    abstract fun bindJavaScriptProvider(impl: ZuperJavaScriptProvider): JavaScriptProvider

    @Binds
    abstract fun bindOperationRunner(impl: JavaScriptOperationRunner): OperationRunner


}
