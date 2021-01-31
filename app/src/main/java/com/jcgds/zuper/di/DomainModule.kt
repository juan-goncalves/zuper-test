package com.jcgds.zuper.di

import com.jcgds.data_layer.repositories.OperationRepositoryImpl
import com.jcgds.domain.repositories.OperationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindOperationRepository(impl: OperationRepositoryImpl): OperationRepository

}
