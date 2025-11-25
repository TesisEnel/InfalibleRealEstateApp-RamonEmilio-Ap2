package com.infaliblerealestate.di

import com.infaliblerealestate.data.remote.Propiedades.PropiedadesApiService
import com.infaliblerealestate.data.remote.Usuarios.UsuariosApiService
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
object AppModule {
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
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://infaliblerealestateapi.up.railway.app/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideJugadorApiService(retrofit: Retrofit): UsuariosApiService {
        return retrofit.create(UsuariosApiService::class.java)
    }

    @Provides
    @Singleton
    fun prividePropiedadesApiService(retrofit: Retrofit): PropiedadesApiService {
        return retrofit.create(PropiedadesApiService::class.java)
    }
}