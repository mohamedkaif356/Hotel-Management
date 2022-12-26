package com.wadachirebandi.admin.di

import com.wadachirebandi.admin.api.EntryApi
import com.wadachirebandi.admin.api.VillaApi
import com.wadachirebandi.admin.data.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideEntryApi(retrofit: Retrofit): EntryApi =
        retrofit.create(EntryApi::class.java)

    @Provides
    @Singleton
    fun provideVillaApi(retrofit: Retrofit): VillaApi =
        retrofit.create(VillaApi::class.java)
}