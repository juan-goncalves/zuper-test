package com.jcgds.zuper.di

import android.content.Context
import com.jcgds.data_layer.local.sources.JavaScriptOperationRunner
import com.jcgds.data_layer.network.services.ZuperAwsService
import com.jcgds.data_layer.network.sources.ZuperJavaScriptProvider
import com.jcgds.data_layer.repositories.OperationRepositoryImpl
import com.jcgds.data_layer.sources.JavaScriptProvider
import com.jcgds.data_layer.sources.OperationRunner
import com.jcgds.domain.repositories.OperationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object DataModule {

    @Provides
    fun provideOperationRepository(
        runner: OperationRunner
    ): OperationRepository = OperationRepositoryImpl(runner)

    @Provides
    fun provideJsDataSource(
        zuperService: ZuperAwsService,
    ): JavaScriptProvider = ZuperJavaScriptProvider(zuperService)

    @Provides
    fun provideOperationDataSource(
        @ApplicationContext context: Context,
        jsProvider: JavaScriptProvider
    ): OperationRunner = JavaScriptOperationRunner(context, jsProvider)

}
