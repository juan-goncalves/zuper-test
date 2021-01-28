package com.jcgds.zuper.di

import android.content.Context
import com.jcgds.data_layer.repositories.FakeOperationRepository
import com.jcgds.data_layer.sources.JavaScriptOperationDataSource
import com.jcgds.data_layer.sources.OperationDataSource
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
    ): OperationRepository = FakeOperationRepository(dataSource)

    @Provides
    fun provideOperationDataSource(
        @ApplicationContext context: Context,
    ): OperationDataSource = JavaScriptOperationDataSource(context)

}
