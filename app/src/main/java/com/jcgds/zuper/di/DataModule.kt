package com.jcgds.zuper.di

import android.content.Context
import com.jcgds.data_layer.local.sources.JavaScriptOperationDataSource
import com.jcgds.data_layer.local.sources.OperationDataSource
import com.jcgds.data_layer.network.services.ZuperAwsService
import com.jcgds.data_layer.network.sources.JavaScriptDataSource
import com.jcgds.data_layer.repositories.OperationRepositoryImpl
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
        dataSource: OperationDataSource
    ): OperationRepository = OperationRepositoryImpl(dataSource)

    @Provides
    fun provideJsDataSource(
        zuperService: ZuperAwsService,
    ): JavaScriptDataSource {
        return JavaScriptDataSource(zuperService)
    }

    @Provides
    fun provideOperationDataSource(
        @ApplicationContext context: Context,
        jsProvider: JavaScriptDataSource
    ): OperationDataSource = JavaScriptOperationDataSource(context, jsProvider)

}
