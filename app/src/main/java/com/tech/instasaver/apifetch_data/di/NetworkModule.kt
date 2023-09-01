package com.tech.instasaver.apifetch_data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tech.instasaver.apifetch_data.retrofit.ApiService
import com.tech.instasaver.apifetch_data.retrofit.ApiService.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


//3
//dependency injection
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun providesApiService(moshi: Moshi): ApiService = Retrofit.Builder().run {
        baseUrl(BASE_URL)
        addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }.create(ApiService::class.java)
}