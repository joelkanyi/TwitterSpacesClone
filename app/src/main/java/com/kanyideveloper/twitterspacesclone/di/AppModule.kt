package com.kanyideveloper.twitterspacesclone.di

import android.app.Application
import com.kanyideveloper.twitterspacesclone.BuildConfig.TOKEN_ENDPOINT
import com.kanyideveloper.twitterspacesclone.data.network.TokenRequestApi
import com.kanyideveloper.twitterspacesclone.data.repository.SpaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import live.hms.video.sdk.HMSSDK
import live.hms.video.sdk.models.enums.HMSAnalyticsEventLevel
import live.hms.video.utils.HMSLogger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)

        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideToken(okHttpClient: OkHttpClient): TokenRequestApi {
        return Retrofit.Builder()
            .baseUrl(TOKEN_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TokenRequestApi::class.java)
    }

    @Singleton
    @Provides
    fun provideHMSSdk(application: Application): HMSSDK {
        return HMSSDK.Builder(application)
            .build()
    }

    @Provides
    @Singleton
    fun provideSpaceRepository(api: TokenRequestApi, hmssdk: HMSSDK): SpaceRepository {
        return SpaceRepository(api, hmssdk)
    }
}