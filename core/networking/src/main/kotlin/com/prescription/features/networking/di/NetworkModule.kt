package com.prescription.features.networking.di

import com.prescription.features.networking.api.MedicineApiService
import com.prescription.features.networking.repository.MedicineRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://run.mocky.io/"

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideProductsApiService(retrofit: Retrofit): MedicineApiService {
        return retrofit.create(MedicineApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductRepository(apiService: MedicineApiService): MedicineRepository {
        return MedicineRepository(apiService)
    }
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IODispatcher
