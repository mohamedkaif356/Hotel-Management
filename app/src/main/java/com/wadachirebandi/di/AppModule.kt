package com.wadachirebandi.di

import com.wadachirebandi.api.EntryApi
import com.wadachirebandi.api.VillaApi
import com.wadachirebandi.data.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
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