package com.jcgds.zuper.di

import android.content.Context
import com.jcgds.data_layer.local.sources.JavaScriptOperationDataSource
import com.jcgds.data_layer.local.sources.LocalOperationDataSource
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
        dataSource: LocalOperationDataSource
    ): OperationRepository = OperationRepositoryImpl(dataSource)

    @Provides
    fun provideOperationDataSource(
        @ApplicationContext context: Context,
    ): LocalOperationDataSource = JavaScriptOperationDataSource(context)

}
