package com.moon.pharm.data.di

import com.moon.pharm.data.BuildConfig
import com.moon.pharm.data.datasource.remote.fcm.FcmApi
import com.moon.pharm.data.datasource.remote.kakao.KakaoApiConst
import com.moon.pharm.data.datasource.remote.kakao.KakaoApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoApiService(
        moshi: Moshi,
        client: OkHttpClient
    ): KakaoApiService {
        return Retrofit.Builder()
            .baseUrl(KakaoApiConst.BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KakaoApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideFcmApi(
        moshi: Moshi,
        client: OkHttpClient
    ): FcmApi {
        val localServerUrl = BuildConfig.FCM_SERVER_URL

        return Retrofit.Builder()
            .baseUrl(localServerUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(FcmApi::class.java)
    }
}