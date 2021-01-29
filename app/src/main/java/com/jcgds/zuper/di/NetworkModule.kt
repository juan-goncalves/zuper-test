package com.jcgds.zuper.di

import com.jcgds.data_layer.network.services.StaticJavascriptService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideStaticJsService(httpClient: OkHttpClient): StaticJavascriptService {
        return Retrofit.Builder()
            .baseUrl("https://s3.eu-central-1.amazonaws.com/getzuper.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClient)
            .build()
            .create(StaticJavascriptService::class.java)
    }

}